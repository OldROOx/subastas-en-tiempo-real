package com.example.subastas_gael_charly.features.auctions.auctions.domain.usescases

import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import javax.inject.Inject

class GetAuctionsUseCase @Inject constructor(
    private val repository: AuctionRepository
) {
    operator fun invoke() = repository.getAuctionsStream()
}