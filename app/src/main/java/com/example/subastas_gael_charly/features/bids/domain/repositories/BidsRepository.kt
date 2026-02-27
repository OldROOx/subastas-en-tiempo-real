package com.example.subastas_gael_charly.features.bids.domain.repositories

import com.example.subastas_gael_charly.features.bids.domain.entities.Bid
import kotlinx.coroutines.flow.Flow

interface BidsRepository {
    fun connect()
    fun disconnect()
    fun joinAuction(auctionId: Int)
    fun placeBid(auctionId: Int, userId: Int, amount: Double)
    fun observeBids(): Flow<Bid>
}