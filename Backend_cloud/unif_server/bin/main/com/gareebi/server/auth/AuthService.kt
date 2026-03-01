package com.gareebi.server.auth

import com.gareebi.server.domain.Role
import com.gareebi.server.domain.User
import com.gareebi.server.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    fun register(request: RegisterRequest): Mono<AuthResponse> {
        // Validate role
        val role = try {
            Role.valueOf(request.role.uppercase())
        } catch (e: IllegalArgumentException) {
            return Mono.error(IllegalArgumentException("Invalid role: ${request.role}. Must be PRODUCER, CONSUMER, or BOTH"))
        }

        return userRepository.existsByEmail(request.email)
            .flatMap { exists ->
                if (exists) {
                    Mono.error(IllegalStateException("Email already registered: ${request.email}"))
                } else {
                    val user = User(
                        email = request.email,
                        passwordHash = passwordEncoder.encode(request.password),
                        role = role.name,
                        walletBalance = java.math.BigDecimal("100.00") // Seed wallet for hackathon demo
                    )
                    userRepository.save(user)
                }
            }
            .map { saved ->
                val token = jwtUtil.generateToken(saved.id!!, saved.email, saved.role)
                AuthResponse(
                    token = token,
                    userId = saved.id.toString(),
                    email = saved.email,
                    role = saved.role
                )
            }
    }

    fun login(request: LoginRequest): Mono<AuthResponse> {
        return userRepository.findByEmail(request.email)
            .switchIfEmpty(Mono.error(IllegalArgumentException("Invalid email or password")))
            .flatMap { user ->
                if (!passwordEncoder.matches(request.password, user.passwordHash)) {
                    Mono.error(IllegalArgumentException("Invalid email or password"))
                } else {
                    val token = jwtUtil.generateToken(user.id!!, user.email, user.role)
                    Mono.just(
                        AuthResponse(
                            token = token,
                            userId = user.id.toString(),
                            email = user.email,
                            role = user.role
                        )
                    )
                }
            }
    }
}

// DTOs
data class RegisterRequest(
    val email: String,
    val password: String,
    val role: String = "CONSUMER"
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val userId: String,
    val email: String,
    val role: String
)
