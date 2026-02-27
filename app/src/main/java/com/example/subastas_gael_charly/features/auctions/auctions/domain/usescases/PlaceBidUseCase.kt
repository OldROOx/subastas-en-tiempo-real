package com.example.subastas_gael_charly.features.auctions.auctions.domain.usescases

import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import javax.inject.Inject

class PlaceBidUseCase @Inject constructor(
    private val repository: AuctionRepository
) {
    suspend operator fun invoke(
        auctionId: Int,
        auctionOwnerId: Int?,
        currentUserId: Int,
        newAmount: Double,
        oldAmount: Double
    ): Result<Unit> {
        if (auctionOwnerId != null && auctionOwnerId == currentUserId) {
            return Result.failure(Exception("No puedes pujar en tu propia subasta"))
        }

        repository.updateLocalPrice(auctionId, newAmount)
        val result = repository.placeBidRemote(auctionId, currentUserId, newAmount)
        if (result.isFailure) {
            repository.updateLocalPrice(auctionId, oldAmount)
        }
        return result
    }
}