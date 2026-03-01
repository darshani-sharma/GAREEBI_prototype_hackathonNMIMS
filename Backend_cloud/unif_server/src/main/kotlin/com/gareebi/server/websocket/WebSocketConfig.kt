package com.gareebi.server.websocket

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class WebSocketConfig(private val energyStreamHandler: EnergyStreamHandler) {

    @Bean
    fun webSocketHandlerMapping(): HandlerMapping {
        val map = mapOf("/ws/energy-stream" to energyStreamHandler)
        val handlerMapping = SimpleUrlHandlerMapping()
        handlerMapping.urlMap = map
        handlerMapping.order = -1 // High priority
        return handlerMapping
    }

    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter = WebSocketHandlerAdapter()
}
