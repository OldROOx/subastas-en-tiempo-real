package com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.api

import com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote.models.AuctionListResponse
import retrofit2.Response
import retrofit2.http.GET

interface AuctionApi {
    @GET("auctions/")
    suspend fun getAuctions(): Response<AuctionListResponse>
}