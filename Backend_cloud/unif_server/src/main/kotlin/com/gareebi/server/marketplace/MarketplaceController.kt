package com.gareebi.server.marketplace

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/v1/marketplace")
class MarketplaceController(private val marketplaceService: MarketplaceService) {

    @GetMapping
    fun getOffers(): Flux<MarketplaceOffer> {
        return marketplaceService.getAvailableOffers()
    }
}
