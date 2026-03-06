package com.example.subastas_gael_charly.features.bids.data.datasources.remote.mapper

import com.example.subastas_gael_charly.features.bids.data.datasources.remote.models.BidDTO
import com.example.subastas_gael_charly.features.bids.domain.entities.Bid

fun BidDTO.toDomain() = Bid(
    id = id,
    auctionId = auction_id,
    userId = user_id,
    amount = amount,
    createdAt = created_at
)