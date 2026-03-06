package com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories

import com.example.subastas_gael_charly.features.auctions.auctions.domain.entities.Auction

interface AuctionDBRepository {
    suspend fun getAllAuctions(): List<Auction>
    suspend fun createAuction(id_user: Int, title: String, current_price: Double, end_time: String)
    suspend fun getAuctionByID(id: Int): Auction
    suspend fun setAuctionStatus(id: Int, status: Boolean)
}