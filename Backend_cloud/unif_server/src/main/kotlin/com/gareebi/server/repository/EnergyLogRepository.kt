package com.gareebi.server.repository

import com.gareebi.server.domain.EnergyLog
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

interface EnergyLogRepository : ReactiveCrudRepository<EnergyLog, UUID> {

    fun findByMeterIdOrderByTimestampDesc(meterId: UUID): Flux<EnergyLog>

    @Query("""
        SELECT COALESCE(SUM(kwh_value), 0)
        FROM energy_logs
        WHERE meter_id = :meterId AND type = 'GENERATION'
    """)
    fun sumGenerationByMeterId(meterId: UUID): Mono<Double>

    @Query("""
        SELECT * FROM energy_logs
        WHERE type = 'GENERATION'
        ORDER BY timestamp DESC
        LIMIT 50
    """)
    fun findLatestGenerationLogs(): Flux<EnergyLog>
}
