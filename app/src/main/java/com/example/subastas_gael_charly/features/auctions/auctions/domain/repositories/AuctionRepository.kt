package com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories

import com.example.subastas_gael_charly.features.auctions.auctions.domain.entities.Auction
import kotlinx.coroutines.flow.Flow

interface AuctionRepository {
    fun getAuctionsStream(): Flow<List<Auction>>
    suspend fun refreshAuctions()
    suspend fun updateLocalPrice(id: Int, price: Double)
    suspend fun placeBidRemote(auctionId: Int, userId: Int, amount: Double): Result<Unit>
    suspend fun createAuction(title: String, currentPrice: Double, endTime: String, userId: Int): Result<Unit>
    fun startRealTimeUpdates()
}