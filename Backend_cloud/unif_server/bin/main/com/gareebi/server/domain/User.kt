package com.gareebi.server.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@Table("users")
data class User(
    @Id
    val id: UUID? = null,
    val email: String,
    @Column("password_hash")
    val passwordHash: String,
    @Column("wallet_balance")
    val walletBalance: BigDecimal = BigDecimal.ZERO,
    @Column("price_per_kwh")
    val pricePerKwh: BigDecimal = BigDecimal("0.50"),
    val role: String = Role.CONSUMER.name,
    @Column("created_at")
    val createdAt: OffsetDateTime? = null
)
