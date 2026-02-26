package com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.models

data class AuctionDto(
    val id: Int,
    val title: String,
    val current_price: Double,
    val end_time: String,
    val status: Boolean,
    val user_id: Int?
)

data class AuctionListResponse(
    val message: String,
    val auctions: List<AuctionDto>
)