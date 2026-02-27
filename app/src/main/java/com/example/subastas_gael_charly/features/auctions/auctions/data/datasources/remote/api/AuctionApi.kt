package com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.api

import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.models.AuctionListResponse
import retrofit2.Response
import retrofit2.http.GET

interface AuctionApi {
    @GET("auctions/")
    suspend fun getAuctions(): Response<AuctionListResponse>

    /*
    @POST("auctions/")
    suspend fun createAuction(@Body request: AuctionRequest): AuctionResponse
    @GET("auctions/")
    suspend fun getAuctions(): AuctionListResponse

    @GET("auctions/{id}")
    suspend fun  getAuctionByID(@Path("id") id: Int): AuctionResponse

    @PATCH("auctions/{id}")
    suspend fun updateAuction(@Path("id") id: Int, @Body request: AuctionStatusRequest): AuctionResponse
     */
}