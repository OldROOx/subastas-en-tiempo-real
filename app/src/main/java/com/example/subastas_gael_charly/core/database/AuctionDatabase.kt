package com.example.subastas_gael_charly.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.subastas_gael_charly.core.database.dao.AuctionDao
import com.example.subastas_gael_charly.core.database.entities.AuctionEntity

@Database(entities = [AuctionEntity::class], version = 1, exportSchema = false)
abstract class AuctionDatabase : RoomDatabase() {
    abstract fun auctionDao(): AuctionDao
}