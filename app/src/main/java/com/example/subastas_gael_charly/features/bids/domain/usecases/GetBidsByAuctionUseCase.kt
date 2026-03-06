package com.example.subastas_gael_charly.features.bids.domain.usecases

import com.example.subastas_gael_charly.features.bids.domain.entities.Bid
import com.example.subastas_gael_charly.features.bids.domain.repositories.BidsRepository
import javax.inject.Inject

class GetBidsByAuctionUseCase @Inject constructor(
    private val repository: BidsRepository
) {
    suspend operator fun invoke(id: Int): List<Bid> =
        repository.getBidsByAuction(id)
}