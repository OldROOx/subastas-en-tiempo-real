package com.example.subastas_gael_charly.features.auctions.auctions.data.repositories

import com.example.subastas_gael_charly.core.database.dao.AuctionDao
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.WebSocketDataSource
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.api.AuctionApi
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.mapper.toDomain
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.mapper.toEntity
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.models.CreateAuctionRequest
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.models.PlaceBidRequest
import com.example.subastas_gael_charly.features.auctions.auctions.domain.entities.Auction
import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuctionRepositoryImpl @Inject constructor(
    private val api: AuctionApi,
    private val dao: AuctionDao,
    private val webSocketDataSource: WebSocketDataSource
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

    override suspend fun placeBidRemote(auctionId: Int, userId: Int, amount: Double): Result<Unit> {
        return try {
            val response = api.placeBid(auctionId, PlaceBidRequest(user_id = userId, amount = amount))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error al pujar: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createAuction(
        title: String,
        currentPrice: Double,
        endTime: String,
        userId: Int
    ): Result<Unit> {
        return try {
            val response = api.createAuction(
                CreateAuctionRequest(
                    title = title,
                    current_price = currentPrice,
                    end_time = endTime,
                    user_id = userId
                )
            )
            if (response.isSuccessful) {
                response.body()?.auction?.let { dto ->
                    dao.insertAuctions(listOf(dto.toEntity()))
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al crear subasta: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun startRealTimeUpdates() {
        webSocketDataSource.connect { id, price ->
            CoroutineScope(Dispatchers.IO).launch {
                dao.updatePrice(id, price)
            }
        }
    }
}