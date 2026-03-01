package com.gareebi.server.marketplace

import com.gareebi.server.repository.EnergyLogRepository
import com.gareebi.server.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.math.BigDecimal

@Service
class MarketplaceService(
    private val userRepository: UserRepository,
    private val energyLogRepository: EnergyLogRepository
) {
    fun getAvailableOffers(): Flux<MarketplaceOffer> {
        return energyLogRepository.findLatestGenerationLogs()
            .flatMap { log ->
                userRepository.findById(log.meterId)
                    .map { producer ->
                        MarketplaceOffer(
                            sellerId = producer.id?.toString(), // FIXED: Safe string conversion
                            sellerEmail = producer.email,
                            availableKwh = log.kwhValue,
                            pricePerKwh = producer.pricePerKwh,
                            totalPrice = producer.pricePerKwh.multiply(BigDecimal.valueOf(log.kwhValue)),
                            lastUpdated = log.timestamp.toString()
                        )
                    }
            }
    }
}

data class MarketplaceOffer(
    val sellerId: String?,
    val sellerEmail: String,
    val availableKwh: Double,
    val pricePerKwh: BigDecimal,
    val totalPrice: BigDecimal,
    val lastUpdated: String
)