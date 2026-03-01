package com.gareebi.server.ingestion

import com.gareebi.server.auth.AuthenticatedUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/telemetry")
class TelemetryController(private val telemetryService: TelemetryService) {

    @PostMapping
    fun ingest(
        @RequestBody request: TelemetryRequest,
        @AuthenticationPrincipal principal: AuthenticatedUser
    ): Mono<ResponseEntity<Any>> {
        return telemetryService.ingestReading(request, principal)
            .map { ResponseEntity.status(HttpStatus.CREATED).body(it as Any) }
            .onErrorResume(SecurityException::class.java) { e ->
                Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(mapOf("error" to (e.message ?: "Forbidden"))))
            }
            .onErrorResume(IllegalArgumentException::class.java) { e ->
                Mono.just(ResponseEntity.badRequest()
                    .body(mapOf("error" to (e.message ?: "Bad request"))))
            }
    }
}
