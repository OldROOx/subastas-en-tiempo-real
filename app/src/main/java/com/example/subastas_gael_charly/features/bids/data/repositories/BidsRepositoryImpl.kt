package com.example.subastas_gael_charly.features.bids.data.repositories

import com.example.subastas_gael_charly.features.bids.data.datasources.remote.mapper.toDomain
import com.example.subastas_gael_charly.features.bids.data.datasources.remote.socket.BidsSocketDataSource
import com.example.subastas_gael_charly.features.bids.domain.entities.Bid
import com.example.subastas_gael_charly.features.bids.domain.repositories.BidsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BidsRepositoryImpl @Inject constructor(
    private val dataSource: BidsSocketDataSource
) : BidsRepository {

    override fun connect() = dataSource.connect()

    override fun disconnect() = dataSource.disconnect()

    override fun joinAuction(auctionId: Int) =
        dataSource.joinAuction(auctionId)

    override fun placeBid(auctionId: Int, userId: Int, amount: Double) =
        dataSource.placeBid(auctionId, userId, amount)

    override fun observeBids(): Flow<Bid> =
        dataSource.bidsFlow.map { it.toDomain() }
}