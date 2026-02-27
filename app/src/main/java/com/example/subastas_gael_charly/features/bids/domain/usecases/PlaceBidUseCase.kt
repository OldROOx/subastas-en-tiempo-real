package com.example.subastas_gael_charly.features.bids.domain.usecases

import com.example.subastas_gael_charly.features.bids.domain.repositories.BidsRepository
import javax.inject.Inject

class PlaceBidUseCase @Inject constructor(
    private val repository: BidsRepository
) {
    operator fun invoke(
        auctionId: Int,
        userId: Int,
        amount: Double
    ) {
        repository.placeBid(auctionId, userId, amount)
    }
}