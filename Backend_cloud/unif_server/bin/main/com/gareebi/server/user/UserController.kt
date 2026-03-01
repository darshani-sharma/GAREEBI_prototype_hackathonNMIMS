package com.gareebi.server.user

import com.gareebi.server.auth.AuthenticatedUser
import com.gareebi.server.repository.TransactionRepository
import com.gareebi.server.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository
) {

    @GetMapping("/me")
    fun getProfile(@AuthenticationPrincipal principal: AuthenticatedUser): Mono<ResponseEntity<UserProfileResponse>> {
        val userId = UUID.fromString(principal.id)

        return Mono.zip(
            userRepository.findById(userId)
                .switchIfEmpty(Mono.error(IllegalStateException("User not found"))),
            transactionRepository.sumCo2SavedByUserId(userId).defaultIfEmpty(0.0)
        ).map { (user, co2Total) ->
            ResponseEntity.ok(
                UserProfileResponse(
                    id = user.id.toString(),
                    email = user.email,
                    role = user.role,
                    walletBalance = user.walletBalance,
                    pricePerKwh = user.pricePerKwh,
                    totalCo2SavedKg = co2Total
                )
            )
        }
    }

    @PutMapping("/me/price")
    fun setPrice(
        @RequestBody request: SetPriceRequest,
        @AuthenticationPrincipal principal: AuthenticatedUser
    ): Mono<ResponseEntity<Any>> {
        val userId = UUID.fromString(principal.id)

        if (request.pricePerKwh <= 0) {
            return Mono.just(ResponseEntity.badRequest().body(mapOf("error" to "Price must be positive")))
        }

        return userRepository.findById(userId)
            .flatMap { user ->
                userRepository.save(user.copy(pricePerKwh = BigDecimal.valueOf(request.pricePerKwh)))
            }
            .map { updated ->
                ResponseEntity.ok(mapOf(
                    "message" to "Price updated",
                    "pricePerKwh" to updated.pricePerKwh
                ) as Any)
            }
            .onErrorResume {
                Mono.just(ResponseEntity.internalServerError()
                    .body(mapOf("error" to "Failed to update price")))
            }
    }
}

data class UserProfileResponse(
    val id: String?,
    val email: String,
    val role: String,
    val walletBalance: BigDecimal,
    val pricePerKwh: BigDecimal,
    val totalCo2SavedKg: Double
)

data class SetPriceRequest(
    val pricePerKwh: Double
)
