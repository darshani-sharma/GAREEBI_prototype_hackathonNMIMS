package com.gareebi.server.trading

import com.gareebi.server.auth.AuthenticatedUser
import com.gareebi.server.domain.Transaction
import com.gareebi.server.ingestion.TelemetryService
import com.gareebi.server.repository.TransactionRepository
import com.gareebi.server.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

@Service
class TradingService(
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository,
    private val redisTemplate: ReactiveRedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    private val transactionalOperator: TransactionalOperator
) {
    // CO2 savings constant from spec: 0.85 kg CO2 per kWh (avg for solar)
    private val co2KgPerKwh = 0.85

    fun executeTrade(request: TradeRequest, principal: AuthenticatedUser): Mono<TradeResponse> {
        val buyerId = UUID.fromString(principal.id)
        val sellerId = request.sellerId

        if (buyerId == sellerId) {
            return Mono.error(IllegalArgumentException("You cannot buy energy from yourself"))
        }

        return Mono.zip(
            userRepository.findById(buyerId)
                .switchIfEmpty(Mono.error(IllegalStateException("Buyer not found"))),
            userRepository.findById(sellerId)
                .switchIfEmpty(Mono.error(IllegalArgumentException("Seller not found: $sellerId")))
        ).flatMap { (buyer, seller) ->
            val totalPrice = seller.pricePerKwh.multiply(BigDecimal.valueOf(request.amountKwh))
                .setScale(8, RoundingMode.HALF_UP)

            if (buyer.walletBalance < totalPrice) {
                return@flatMap Mono.error(
                    IllegalStateException(
                        "Insufficient wallet balance. Required: $totalPrice, Available: ${buyer.walletBalance}"
                    )
                )
            }

            val co2Saved = request.amountKwh * co2KgPerKwh

            // Atomic R2DBC transaction: deduct buyer, credit seller, save record
            val transactionFlow = Mono.defer {
                val updatedBuyer = buyer.copy(walletBalance = buyer.walletBalance.subtract(totalPrice))
                val updatedSeller = seller.copy(walletBalance = seller.walletBalance.add(totalPrice))

                val record = Transaction(
                    buyerId = buyerId,
                    sellerId = sellerId,
                    amountKwh = request.amountKwh,
                    pricePaid = totalPrice,
                    co2SavedKg = co2Saved
                )

                userRepository.save(updatedBuyer)
                    .then(userRepository.save(updatedSeller))
                    .then(transactionRepository.save(record))
            }.`as`(transactionalOperator::transactional)

            transactionFlow.flatMap { savedTx ->
                // Publish trade event to Redis for WebSocket broadcast
                val event = TradeEvent(
                    eventType = "TRADE",
                    transactionId = savedTx.id.toString(),
                    buyerId = buyerId.toString(),
                    sellerId = sellerId.toString(),
                    amountKwh = request.amountKwh,
                    pricePaid = totalPrice.toDouble(),
                    co2SavedKg = co2Saved
                )
                val payload = objectMapper.writeValueAsString(event)
                redisTemplate.convertAndSend(TelemetryService.REDIS_CHANNEL, payload)
                    .thenReturn(
                        TradeResponse(
                            transactionId = savedTx.id.toString(),
                            amountKwh = request.amountKwh,
                            pricePaid = totalPrice,
                            co2SavedKg = co2Saved,
                            newWalletBalance = buyer.walletBalance.subtract(totalPrice),
                            message = "Trade executed successfully"
                        )
                    )
            }
        }
    }
}

// DTOs
data class TradeRequest(
    val sellerId: UUID,
    val amountKwh: Double
)

data class TradeResponse(
    val transactionId: String?,
    val amountKwh: Double,
    val pricePaid: BigDecimal,
    val co2SavedKg: Double,
    val newWalletBalance: BigDecimal,
    val message: String
)

data class TradeEvent(
    val eventType: String,
    val transactionId: String?,
    val buyerId: String,
    val sellerId: String,
    val amountKwh: Double,
    val pricePaid: Double,
    val co2SavedKg: Double
)
