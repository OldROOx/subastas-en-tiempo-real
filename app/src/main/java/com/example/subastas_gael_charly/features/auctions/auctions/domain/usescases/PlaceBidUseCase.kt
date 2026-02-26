package com.example.subastas_gael_charly.features.auctions.auctions.domain.usescases

import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import javax.inject.Inject

class PlaceBidUseCase @Inject constructor(
    private val repository: AuctionRepository
) {
    suspend operator fun invoke(auctionId: Int, newAmount: Double, oldAmount: Double): Result<Unit> {
        repository.updateLocalPrice(auctionId, newAmount)
        val result = repository.placeBidRemote(auctionId, newAmount)
        if (result.isFailure) {
            repository.updateLocalPrice(auctionId, oldAmount)
        }
        return result
    }
}