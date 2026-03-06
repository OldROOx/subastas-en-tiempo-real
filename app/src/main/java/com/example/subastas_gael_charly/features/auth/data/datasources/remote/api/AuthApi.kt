package com.example.subastas_gael_charly.features.auth.data.datasources.remote.api

import com.example.subastas_gael_charly.features.auth.data.datasources.remote.models.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("users/")
    suspend fun register(@Body request: LoginRequest): Response<MessageResponse>

    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<UserDto>

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body request: LoginRequest): Response<MessageResponse>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<MessageResponse>
}