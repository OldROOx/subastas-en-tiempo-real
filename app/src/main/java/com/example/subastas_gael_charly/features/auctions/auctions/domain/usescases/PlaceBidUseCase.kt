package com.example.subastas_gael_charly.features.auctions.auctions.domain.usescases

import com.example.subastas_gael_charly.features.auctions.domain.repositories.AuctionRepository
import javax.inject.Inject

class PlaceBidUseCase @Inject constructor(
    private val repository: AuctionRepository
) {
    suspend operator fun invoke(auctionId: Int, newAmount: Double, oldAmount: Double): Result<Unit> {
        // 1. PASO OPTIMISTA: Actualizamos la DB local inmediatamente
        repository.updateLocalPrice(auctionId, newAmount)

        // 2. LLAMADA AL SERVIDOR
        val result = repository.placeBidRemote(auctionId, newAmount)

        // 3. PASO ROLLBACK: Si el servidor falla, regresamos al precio anterior
        if (result.isFailure) {
            repository.updateLocalPrice(auctionId, oldAmount)
        }

        return result
    }
}