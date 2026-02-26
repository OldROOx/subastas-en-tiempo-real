package com.example.subastas_gael_charly.features.auctions.auctions.data.di

import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.api.AuctionApi
import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuctionModule {

    @Binds
    @Singleton
    abstract fun bindAuctionRepository(impl: AuctionRepositoryImpl): AuctionRepository

    companion object {
        @Provides
        @Singleton
        fun provideAuctionApi(retrofit: Retrofit): AuctionApi {
            return retrofit.create(AuctionApi::class.java)
        }
    }
}