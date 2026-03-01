package com.gareebi.server.trading

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
@RequestMapping("/api/v1/trade")
class TradingController(private val tradingService: TradingService) {

    @PostMapping
    fun trade(
        @RequestBody request: TradeRequest,
        @AuthenticationPrincipal principal: AuthenticatedUser
    ): Mono<ResponseEntity<Any>> {
        return tradingService.executeTrade(request, principal)
            .map { ResponseEntity.status(HttpStatus.CREATED).body(it as Any) }
            .onErrorResume(IllegalStateException::class.java) { e ->
                Mono.just(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(mapOf("error" to (e.message ?: "Trade failed"))))
            }
            .onErrorResume(IllegalArgumentException::class.java) { e ->
                Mono.just(ResponseEntity.badRequest()
                    .body(mapOf("error" to (e.message ?: "Bad request"))))
            }
    }
}
