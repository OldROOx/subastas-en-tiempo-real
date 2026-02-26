package com.example.subastas_gael_charly.features.auctions.data.repositories

import com.example.subastas_gael_charly.core.database.dao.AuctionDao
import com.example.subastas_gael_charly.features.auctions.data.datasources.remote.WebSocketDataSource
import com.example.subastas_gael_charly.features.auctions.data.datasources.remote.api.AuctionApi
import com.example.subastas_gael_charly.features.auctions.data.datasources.remote.mapper.toDomain
import com.example.subastas_gael_charly.features.auctions.data.datasources.remote.mapper.toEntity
import com.example.subastas_gael_charly.features.auctions.domain.repositories.AuctionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuctionRepositoryImpl @Inject constructor(
    private val api: AuctionApi,
    private val dao: AuctionDao,
    private val webSocketDataSource: WebSocketDataSource
) : AuctionRepository {

    override fun getAuctionsStream() = dao.getAllAuctions().map { it.map { e -> e.toDomain() } }

    override suspend fun refreshAuctions() {
        val response = api.getAuctions()
        if (response.isSuccessful) {
            dao.insertAuctions(response.body()?.auctions?.map { it.toEntity() } ?: emptyList())
        }
    }

    // NUEVO: Iniciar escucha de tiempo real
    fun startRealTimeUpdates() {
        webSocketDataSource.connect { id, price ->
            // Al llegar algo del socket, lo mandamos a la DB (SSOT)
            CoroutineScope(Dispatchers.IO).launch {
                dao.updatePrice(id, price)
            }
        }
    }

    override suspend fun updateLocalPrice(id: Int, price: Double) = dao.updatePrice(id, price)

    override suspend fun placeBidRemote(id: Int, amount: Double): Result<Unit> {
        // Implementar POST a tu API de pujas aquí
        return Result.success(Unit)
    }
}