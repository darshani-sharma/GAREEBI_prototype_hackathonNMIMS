package com.gareebi.server.repository

import com.gareebi.server.domain.Transaction
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface TransactionRepository : ReactiveCrudRepository<Transaction, UUID> {

    fun findByBuyerIdOrderByExecutedAtDesc(buyerId: UUID): Flux<Transaction>

    fun findBySellerIdOrderByExecutedAtDesc(sellerId: UUID): Flux<Transaction>

    @Query("""
        SELECT COALESCE(SUM(co2_saved_kg), 0)
        FROM transactions
        WHERE buyer_id = :userId OR seller_id = :userId
    """)
    fun sumCo2SavedByUserId(userId: UUID): Mono<Double>
}
