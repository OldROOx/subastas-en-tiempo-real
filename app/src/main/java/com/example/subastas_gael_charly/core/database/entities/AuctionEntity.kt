package com.example.subastas_gael_charly.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auctions")
data class AuctionEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val currentPrice: Double,
    val endTime: String,
    val status: Boolean
)