package com.gareebi.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.reactive.TransactionalOperator

@Configuration
class InfrastructureConfig {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper().registerKotlinModule()

    // Redis template with String key/value serializers
    @Bean
    fun reactiveRedisTemplate(
        connectionFactory: ReactiveRedisConnectionFactory
    ): ReactiveRedisTemplate<String, String> {
        val stringSerializer = StringRedisSerializer()
        val context = RedisSerializationContext
            .newSerializationContext<String, String>(stringSerializer)
            .value(stringSerializer)
            .build()
        return ReactiveRedisTemplate(connectionFactory, context)
    }

    // Redis Pub/Sub message listener container
    @Bean
    fun reactiveRedisMessageListenerContainer(
        connectionFactory: ReactiveRedisConnectionFactory
    ): ReactiveRedisMessageListenerContainer {
        return ReactiveRedisMessageListenerContainer(connectionFactory)
    }

    // R2DBC transaction manager for atomic trading operations
    @Bean
    fun transactionManager(connectionFactory: ConnectionFactory): R2dbcTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }

    // Declarative reactive transaction support
    @Bean
    fun transactionalOperator(transactionManager: R2dbcTransactionManager): TransactionalOperator {
        return TransactionalOperator.create(transactionManager)
    }
}
