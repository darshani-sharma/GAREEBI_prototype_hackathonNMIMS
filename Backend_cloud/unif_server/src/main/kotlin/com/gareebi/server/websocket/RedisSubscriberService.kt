package com.gareebi.server.websocket

import com.gareebi.server.ingestion.TelemetryService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.data.redis.connection.ReactiveSubscription
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class RedisSubscriberService(
    private val messageListenerContainer: ReactiveRedisMessageListenerContainer,
    private val energyStreamHandler: EnergyStreamHandler,
    private val objectMapper: ObjectMapper
) {
    @EventListener(ApplicationReadyEvent::class)
    fun startListening() {
        val topic = ChannelTopic(TelemetryService.REDIS_CHANNEL)

        messageListenerContainer
            .receive(topic)
            .map { message: ReactiveSubscription.Message<String, String> -> message.message }
            .subscribe { payload ->
                // Forward raw JSON payload directly to the WebSocket broadcast
                energyStreamHandler.broadcast(payload)
            }
    }
}
