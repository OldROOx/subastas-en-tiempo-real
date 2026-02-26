package com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote

import com.google.gson.Gson
import okhttp3.*
import javax.inject.Inject

data class PriceUpdate(val auctionId: Int, val newPrice: Double)

class WebSocketDataSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) {
    private var webSocket: WebSocket? = null

    fun connect(onPriceUpdate: (Int, Double) -> Unit) {
        val request = Request.Builder().url("ws://10.0.2.2:3000").build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val update = gson.fromJson(text, PriceUpdate::class.java)
                    onPriceUpdate(update.auctionId, update.newPrice)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                t.printStackTrace()
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "App closed")
    }
}