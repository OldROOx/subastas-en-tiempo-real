package com.example.subastas_gael_charly.core.di

import android.content.Context
import androidx.room.Room
import com.example.subastas_gael_charly.core.database.AuctionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AuctionDatabase {
        return Room.databaseBuilder(
            context,
            AuctionDatabase::class.java,
            "auctions_db"
        ).build()
    }

    @Provides
    fun provideAuctionDao(db: AuctionDatabase) = db.auctionDao()
}