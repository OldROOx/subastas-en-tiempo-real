package com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.models

data class AuctionDto(
    val id: Int,
    val title: String,
    val current_price: Double,
    val end_time: String,
    val status: Boolean,
    val user_id: Int?
)

data class AuctionRequest(
    val title: String,
    val current_price: Double,
    val end_time: String,
    val user_id: Int,
)

data class AuctionStatusRequest(
    val status: Boolean
)

data class AuctionListResponse(
    val message: String,
    val auctions: List<AuctionDto>
)

data class AuctionResponse(
    val message: String,
    val auction: AuctionDto
)

data class CreateAuctionRequest(
    val title: String,
    val current_price: Double,
    val end_time: String,
    val user_id: Int
)

data class PatchStatusRequest(
    val status: Boolean
)

data class BidDto(
    val id: Int,
    val auction_id: Int,
    val user_id: Int,
    val amount: Double,
    val created_at: String
)

data class BidListResponse(
    val message: String,
    val bids: List<BidDto>
)

data class PlaceBidRequest(
    val user_id: Int,
    val amount: Double
)