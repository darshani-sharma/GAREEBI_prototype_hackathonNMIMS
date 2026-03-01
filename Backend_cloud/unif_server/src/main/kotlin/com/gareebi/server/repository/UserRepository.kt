package com.gareebi.server.repository

import com.gareebi.server.domain.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.util.UUID

interface UserRepository : ReactiveCrudRepository<User, UUID> {
    fun findByEmail(email: String): Mono<User>
    fun existsByEmail(email: String): Mono<Boolean>
}
