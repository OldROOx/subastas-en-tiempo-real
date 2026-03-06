package com.example.subastas_gael_charly.features.auctions.auctions.domain.usescases

import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import javax.inject.Inject

class GetAuctionByIDUseCase @Inject constructor(
    private val repository: AuctionRepository
) {
    suspend operator fun invoke(id: Int) = repository.getAuctionById(id)
}