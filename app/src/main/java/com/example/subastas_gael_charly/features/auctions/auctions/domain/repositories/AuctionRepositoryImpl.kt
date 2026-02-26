package com.example.subastas_gael_charly.features.auctions.auctions.data.repositories

import com.example.subastas_gael_charly.core.database.dao.AuctionDao
import com.example.subastas_gael_charly.features.auctions.data.datasources.remote.api.AuctionApi
import com.example.subastas_gael_charly.features.auctions.data.datasources.remote.mapper.toDomain
import com.example.subastas_gael_charly.features.auctions.data.datasources.remote.mapper.toEntity
import com.example.subastas_gael_charly.features.auctions.domain.entities.Auction
import com.example.subastas_gael_charly.features.auctions.domain.repositories.AuctionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuctionRepositoryImpl @Inject constructor(
    private val api: AuctionApi,
    private val dao: AuctionDao
) : AuctionRepository {

    override fun getAuctionsStream(): Flow<List<Auction>> =
        dao.getAllAuctions().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshAuctions() {
        val response = api.getAuctions()
        if (response.isSuccessful) {
            val entities = response.body()?.auctions?.map { it.toEntity() } ?: emptyList()
            dao.insertAuctions(entities)
        }
    }

    override suspend fun updateLocalPrice(id: Int, price: Double) {
        dao.updatePrice(id, price)
    }

    override suspend fun placeBidRemote(id: Int, amount: Double): Result<Unit> {
        return try {
            // Aquí llamarías a tu endpoint de POST bids/
            // Por ahora simulamos con el API de subastas
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}