package com.example.subastas_gael_charly.features.auctions.auctions.domain.usescases

import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import javax.inject.Inject

class CreateAuctionUseCase @Inject constructor(
    private val repository: AuctionRepository
) {
    suspend operator fun invoke(
        title: String,
        currentPrice: Double,
        endTime: String,
        userId: Int
    ): Result<Unit> {
        if (title.isBlank()) return Result.failure(Exception("El título no puede estar vacío"))
        if (currentPrice <= 0) return Result.failure(Exception("El precio debe ser mayor a 0"))
        return repository.createAuction(title, currentPrice, endTime, userId)
    }
}