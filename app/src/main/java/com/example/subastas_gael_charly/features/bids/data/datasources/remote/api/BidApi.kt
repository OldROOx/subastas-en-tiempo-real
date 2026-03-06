package com.example.subastas_gael_charly.features.bids.data.datasources.remote.api

import com.example.subastas_gael_charly.features.bids.data.datasources.remote.models.BidResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BidApi {
    @GET("auctions/{id}/bids")
    suspend fun getBidsByAuction(@Path("id") id: Int): Response<BidResponse>
}