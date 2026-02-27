package com.example.subastas_gael_charly.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {
    @Provides
    @Singleton
    fun provideSocket(): Socket {
        val options = IO.Options()
        return IO.socket("http://52.5.211.245", options)
    }
}