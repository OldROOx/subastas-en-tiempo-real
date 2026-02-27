package com.example.subastas_gael_charly.features.bids.data.datasources.remote.models

data class BidDTO(
    val id: Int,
    val auction_id: Int,
    val user_id: Int,
    val amount: Double,
    val created_at: String,
)