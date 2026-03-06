package com.example.subastas_gael_charly.features.auctions.auctions.data.repositories

import com.example.subastas_gael_charly.core.database.dao.AuctionDao
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.api.AuctionApi
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.mapper.toDomain
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.mapper.toEntity
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.models.CreateAuctionRequest
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.models.PlaceBidRequest
import com.example.subastas_gael_charly.features.auctions.auctions.domain.entities.Auction
import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import com.example.subastas_gael_charly.features.bids.data.datasources.remote.socket.BidsSocketDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuctionRepositoryImpl @Inject constructor(
    private val api: AuctionApi,
    private val dao: AuctionDao,
    private val bidsSocketDataSource: BidsSocketDataSource
) : AuctionRepository {

    // Scope propio para no depender de ningún ViewModel
    private val repoScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun getAuctionsStream(): Flow<List<Auction>> =
        dao.getAllAuctions().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshAuctions() {
        val response = api.getAuctions()
        if (response.isSuccessful) {
            val entities = response.body()?.auctions?.map { it.toEntity() } ?: emptyList()
            dao.insertAuctions(entities)
        }
    }

    override suspend fun getAuctionById(id: Int): Auction {
        val response = api.getAuctionById(id)
        return if (response.isSuccessful) {
            response.body()?.auction?.toEntity()?.toDomain()
                ?: throw Exception("Auction not found")
        } else {
            throw Exception("Error al obtener la subasta: ${response.code()}")
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
        title: String, currentPrice: Double, endTime: String, userId: Int
    ): Result<Unit> {
        return try {
            val response = api.createAuction(
                CreateAuctionRequest(title, currentPrice, endTime, userId)
            )
            if (response.isSuccessful) {
                response.body()?.auction?.let { dao.insertAuctions(listOf(it.toEntity())) }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al crear subasta: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun startRealTimeUpdates() {
        // 1. Conectar el socket (idempotente, no conecta doble)
        bidsSocketDataSource.connect()

        // 2. Escuchar TODOS los new_bid que lleguen y actualizar Room
        //    Como getAuctionsStream() es un Flow de Room, la UI se recompone sola
        repoScope.launch {
            bidsSocketDataSource.bidsFlow.collect { bid ->
                dao.updatePrice(bid.auction_id, bid.amount)
            }
        }
    }
}