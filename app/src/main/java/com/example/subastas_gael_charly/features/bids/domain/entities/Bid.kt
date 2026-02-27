package com.example.subastas_gael_charly.features.bids.domain.entities

data class Bid(
    val id: Int,
    val auctionId: Int,
    val userId: Int,
    val amount: Double,
    val createdAt: String
)