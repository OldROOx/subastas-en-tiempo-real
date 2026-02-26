package com.example.subastas_gael_charly.features.auctions.data.datasources.remote

import com.google.gson.Gson
import okhttp3.*
import javax.inject.Inject

// Clase para parsear el mensaje que llega por socket
data class PriceUpdate(val auctionId: Int, val newPrice: Double)

class WebSocketDataSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) {
    private var webSocket: WebSocket? = null

    fun connect(onPriceUpdate: (Int, Double) -> Unit) {
        // Cambia la IP si usas dispositivo físico. 10.0.2.2 es para el emulador.
        val request = Request.Builder().url("ws://10.0.2.2:3000").build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    // Asumimos que el servidor envía un JSON: {"auctionId": 1, "newPrice": 50.5}
                    val update = gson.fromJson(text, PriceUpdate::class.java)
                    onPriceUpdate(update.auctionId, update.newPrice)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // Intentar reconectar o manejar el error
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "App closed")
    }
}