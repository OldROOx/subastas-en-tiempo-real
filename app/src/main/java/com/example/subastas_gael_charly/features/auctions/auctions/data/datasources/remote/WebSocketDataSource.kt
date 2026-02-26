package com.example.subastas_gael_charly.features.auctions.auctions.data.datasources.remote

import okhttp3.*
import javax.inject.Inject

class WebSocketDataSource @Inject constructor(
    private val client: OkHttpClient
) {
    private var webSocket: WebSocket? = null

    fun connect(onPriceUpdate: (auctionId: Int, newPrice: Double) -> Unit) {
        val request = Request.Builder().url("ws://10.0.2.2:3000").build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                // Aquí parseas el JSON de la puja que llega de Node.js
                // y llamas a onPriceUpdate
            }
        })
    }
}