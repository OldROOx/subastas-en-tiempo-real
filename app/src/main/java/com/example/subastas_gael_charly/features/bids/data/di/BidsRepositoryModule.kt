package com.example.subastas_gael_charly.features.bids.data.di

import com.example.subastas_gael_charly.features.bids.data.datasources.remote.api.BidApi
import com.example.subastas_gael_charly.features.bids.data.repositories.BidsRepositoryImpl
import com.example.subastas_gael_charly.features.bids.domain.repositories.BidsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BidsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBidsRepository(
        impl: BidsRepositoryImpl
    ): BidsRepository

    companion object {
        @Provides
        @Singleton
        fun provideBidApi(retrofit: Retrofit): BidApi {
            return retrofit.create(BidApi::class.java)
        }
    }
}