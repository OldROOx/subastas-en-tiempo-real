package com.example.subastas_gael_charly.core.database.dao

import androidx.room.*
import com.example.subastas_gael_charly.core.database.entities.AuctionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuctionDao {
    @Query("SELECT * FROM auctions ORDER BY id DESC")
    fun getAllAuctions(): Flow<List<AuctionEntity>> // Reactividad pura

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuctions(auctions: List<AuctionEntity>)

    @Query("UPDATE auctions SET currentPrice = :newPrice WHERE id = :auctionId")
    suspend fun updatePrice(auctionId: Int, newPrice: Double)
}