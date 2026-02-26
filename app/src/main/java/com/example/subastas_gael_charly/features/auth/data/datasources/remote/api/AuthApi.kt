package com.example.subastas_gael_charly.features.auth.data.datasources.remote.api

import com.example.subastas_gael_charly.features.auth.data.datasources.remote.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("users/")
    suspend fun register(@Body request: LoginRequest): Response<MessageResponse>

    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}