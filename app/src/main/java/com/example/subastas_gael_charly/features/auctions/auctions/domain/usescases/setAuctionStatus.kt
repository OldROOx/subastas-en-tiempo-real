package com.example.subastas_gael_charly.features.auctions.auctions.domain.usescases

import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import javax.inject.Inject

class setAuctionStatus @Inject constructor(
    private val repository: AuctionRepository
) {
    suspend fun invoke(id: Int, status: Boolean) = repository.setAuctionStatus(id, status)
}