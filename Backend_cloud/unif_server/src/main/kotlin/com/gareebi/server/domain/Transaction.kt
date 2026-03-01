package com.gareebi.server.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@Table("transactions")
data class Transaction(
    @Id
    val id: UUID? = null,
    @Column("buyer_id")
    val buyerId: UUID,
    @Column("seller_id")
    val sellerId: UUID,
    @Column("amount_kwh")
    val amountKwh: Double,
    @Column("price_paid")
    val pricePaid: BigDecimal,
    @Column("co2_saved_kg")
    val co2SavedKg: Double,
    @Column("executed_at")
    val executedAt: OffsetDateTime? = null
)
