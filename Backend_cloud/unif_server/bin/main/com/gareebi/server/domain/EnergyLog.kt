package com.gareebi.server.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.UUID

@Table("energy_logs")
data class EnergyLog(
    @Id
    val id: UUID? = null,
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
    @Column("meter_id")
    val meterId: UUID,
    @Column("kwh_value")
    val kwhValue: Double,
    val type: String // GENERATION or CONSUMPTION
)
