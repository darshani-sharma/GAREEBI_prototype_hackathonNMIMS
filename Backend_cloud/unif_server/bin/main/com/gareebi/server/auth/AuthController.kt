package com.gareebi.server.auth

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): Mono<ResponseEntity<Any>> {
        return authService.register(request)
            .map { ResponseEntity.status(HttpStatus.CREATED).body(it as Any) }
            .onErrorResume(IllegalStateException::class.java) { e ->
                Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(errorBody(e.message ?: "Conflict")))
            }
            .onErrorResume(IllegalArgumentException::class.java) { e ->
                Mono.just(ResponseEntity.badRequest().body(errorBody(e.message ?: "Bad request")))
            }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): Mono<ResponseEntity<Any>> {
        return authService.login(request)
            .map { ResponseEntity.ok(it as Any) }
            .onErrorResume(IllegalArgumentException::class.java) { e ->
                Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(errorBody(e.message ?: "Unauthorized")))
            }
    }

    private fun errorBody(message: String): Map<String, String> = mapOf("error" to message)
}
