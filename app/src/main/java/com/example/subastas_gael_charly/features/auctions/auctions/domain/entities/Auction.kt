package com.example.subastas_gael_charly.features.auctions.auctions.domain.entities

data class Auction(
    val id: Int,
    val title: String,
    val currentPrice: Double,
    val endTime: String,
    val status: Boolean,
    val userId: Int?
)