package com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.mapper

import com.example.subastas_gael_charly.core.database.entities.AuctionEntity
import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.models.AuctionDto
import com.example.subastas_gael_charly.features.auctions.auctions.domain.entities.Auction

fun AuctionDto.toEntity() = AuctionEntity(
    id = id,
    title = title,
    currentPrice = current_price,
    endTime = end_time,
    status = status
)

fun AuctionEntity.toDomain() = Auction(
    id = id,
    title = title,
    currentPrice = currentPrice,
    endTime = endTime,
    status = status,
    userId = null
)