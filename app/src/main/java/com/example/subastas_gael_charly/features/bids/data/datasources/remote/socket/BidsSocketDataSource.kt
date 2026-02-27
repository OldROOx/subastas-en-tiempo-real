package com.example.subastas_gael_charly.features.bids.data.datasources.remote.socket

import android.util.Log
import com.example.subastas_gael_charly.features.bids.data.datasources.remote.models.BidDTO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

class BidsSocketDataSource @Inject constructor(
    private val socket: Socket
) {

    private val _bidsFlow = MutableSharedFlow<BidDTO>()
    val bidsFlow: SharedFlow<BidDTO> = _bidsFlow

    fun connect() {
        socket.connect()
    }

    fun disconnect() {
        socket.disconnect()
    }

    fun joinAuction(auctionId: Int) {
        socket.emit("join_auction", auctionId)
    }

    fun placeBid(auctionId: Int, userId: Int, amount: Double) {
        val json = JSONObject().apply {
            put("auction_id", auctionId)
            put("user_id", userId)
            put("amount", amount)
        }
        socket.emit("place_bid", json)
    }

    fun observeBids() {
        socket.on("new_bid") { args ->
            Log.d("SOCKET", "RAW EVENT: ${args.joinToString()}")
            if (args.isNotEmpty()) {
                val json = args[0] as JSONObject

                val bid = BidDTO(
                    id = json.getInt("id"),
                    auction_id = json.getInt("auction_id"),
                    user_id = json.getInt("user_id"),
                    amount = json.getDouble("amount"),
                    created_at = json.getString("created_at")
                )

                CoroutineScope(Dispatchers.IO).launch {
                    _bidsFlow.emit(bid)
                }
            }
        }
    }
}