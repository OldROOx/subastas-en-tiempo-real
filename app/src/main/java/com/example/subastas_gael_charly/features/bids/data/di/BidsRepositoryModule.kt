package com.example.subastas_gael_charly.features.bids.data.di

import com.example.subastas_gael_charly.features.bids.data.repositories.BidsRepositoryImpl
import com.example.subastas_gael_charly.features.bids.domain.repositories.BidsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BidsRepositoryModule {

    @Binds
    abstract fun bindBidsRepository(
        impl: BidsRepositoryImpl
    ): BidsRepository
}