package com.gareebi.server.ingestion

import com.gareebi.server.auth.AuthenticatedUser
import com.gareebi.server.domain.EnergyLog
import com.gareebi.server.domain.EnergyType
import com.gareebi.server.repository.EnergyLogRepository
import com.gareebi.server.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.UUID

@Service
class TelemetryService(
    private val energyLogRepository: EnergyLogRepository,
    private val userRepository: UserRepository,
    private val redisTemplate: ReactiveRedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
    fun ingestReading(request: TelemetryRequest, principal: AuthenticatedUser): Mono<TelemetryResponse> {
        val userId = UUID.fromString(principal.id)

        // Validate that the meterId belongs to the authenticated producer
        return userRepository.findById(request.meterId)
            .switchIfEmpty(Mono.error(IllegalArgumentException("Meter not found: ${request.meterId}")))
            .flatMap { meterOwner ->
                if (meterOwner.id != userId) {
                    Mono.error(SecurityException("You do not own meter: ${request.meterId}"))
                } else {
                    val log = EnergyLog(
                        meterId = request.meterId,
                        kwhValue = request.kwhValue,
                        type = EnergyType.valueOf(request.type.uppercase()).name,
                        timestamp = OffsetDateTime.now()
                    )
                    energyLogRepository.save(log)
                }
            }
            .flatMap { savedLog ->
                // Publish event to Redis channel for WebSocket broadcast
                val event = EnergyEvent(
                    eventType = "TELEMETRY",
                    meterId = savedLog.meterId.toString(),
                    kwhValue = savedLog.kwhValue,
                    type = savedLog.type,
                    timestamp = savedLog.timestamp.toString()
                )
                val payload = objectMapper.writeValueAsString(event)
                redisTemplate.convertAndSend(REDIS_CHANNEL, payload)
                    .thenReturn(
                        TelemetryResponse(
                            logId = savedLog.id.toString(),
                            meterId = savedLog.meterId.toString(),
                            kwhValue = savedLog.kwhValue,
                            type = savedLog.type,
                            timestamp = savedLog.timestamp.toString(),
                            message = "Reading ingested successfully"
                        )
                    )
            }
    }

    companion object {
        const val REDIS_CHANNEL = "energy-events"
    }
}

// DTOs
data class TelemetryRequest(
    val meterId: UUID,
    val kwhValue: Double,
    val type: String // GENERATION or CONSUMPTION
)

data class TelemetryResponse(
    val logId: String?,
    val meterId: String,
    val kwhValue: Double,
    val type: String,
    val timestamp: String,
    val message: String
)

data class EnergyEvent(
    val eventType: String,
    val meterId: String,
    val kwhValue: Double,
    val type: String,
    val timestamp: String
)
