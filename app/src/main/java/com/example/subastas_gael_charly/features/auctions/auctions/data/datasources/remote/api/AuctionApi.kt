package com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.api

import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.models.*
import retrofit2.Response
import retrofit2.http.*

interface AuctionApi {
    @GET("auctions/")
    suspend fun getAuctions(): Response<AuctionListResponse>

    @GET("auctions/{id}")
    suspend fun getAuctionById(@Path("id") id: Int): Response<AuctionResponse>

    @POST("auctions/")
    suspend fun createAuction(@Body request: CreateAuctionRequest): Response<AuctionResponse>

    @PATCH("auctions/{id}")
    suspend fun patchAuctionStatus(@Path("id") id: Int, @Body request: PatchStatusRequest): Response<AuctionResponse>

    @GET("auctions/{id}/bids")
    suspend fun getBidsByAuction(@Path("id") id: Int): Response<BidListResponse>

    @POST("auctions/{id}/bids")
    suspend fun placeBid(@Path("id") id: Int, @Body request: PlaceBidRequest): Response<BidListResponse>
}