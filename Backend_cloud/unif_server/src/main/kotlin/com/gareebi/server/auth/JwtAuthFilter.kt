package com.gareebi.server.auth

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class JwtAuthFilter(private val jwtUtil: JwtUtil) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange)
        }

        val token = authHeader.removePrefix("Bearer ").trim()
        val claims = jwtUtil.validateTokenAndGetClaims(token)
            ?: return chain.filter(exchange)

        val userId = claims.subject
        val role = claims.get("role", String::class.java)
        val email = claims.get("email", String::class.java)

        val authorities = listOf(SimpleGrantedAuthority("ROLE_$role"))
        val auth = UsernamePasswordAuthenticationToken(
            AuthenticatedUser(userId, email, role),
            null,
            authorities
        )

        // FIXED: Correctly establish the security context for WebFlux
        return chain.filter(exchange)
            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
    }
}

data class AuthenticatedUser(
    val id: String,
    val email: String,
    val role: String
)