package com.gareebi.server.websocket

import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks

/**
 * WebSocket handler using a single multicast Sinks.Many to fan-out to all
 * connected clients without creating a new stream per user (per spec mitigation).
 *
 * Sinks.Many with multicast() means ALL subscribers get each emitted item.
 * A new WebSocket client simply subscribes to the shared hot flux.
 */
@Component
class EnergyStreamHandler : WebSocketHandler {

    // Single shared hot multicast sink — all connected WS sessions subscribe to this
    val sink: Sinks.Many<String> = Sinks.many().multicast().onBackpressureBuffer()

    override fun handle(session: WebSocketSession): Mono<Void> {
        val outbound = session.send(
            sink.asFlux()
                .map { payload -> session.textMessage(payload) }
        )
        return outbound
    }

    /**
     * Emit an event to all connected WebSocket clients.
     * Called by [RedisSubscriberService] when an event arrives from Redis.
     */
    fun broadcast(message: String) {
        val result = sink.tryEmitNext(message)
        if (result.isFailure) {
            // On failure (no subscribers), this is acceptable — just log
            println("[WS] Broadcast dropped (no subscribers): $result")
        }
    }
}
