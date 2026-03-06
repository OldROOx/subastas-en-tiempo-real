package com.example.subastas_gael_charly.features.bids.presentation.screens

import com.example.subastas_gael_charly.features.auctions.auctions.domain.entities.Auction
import com.example.subastas_gael_charly.features.bids.domain.entities.Bid

data class BidsUIState(
    val bids: List<Bid> = emptyList(),
    val auction: Auction? = null,
    val currentInput: String = "",
    val isConnected: Boolean = false,
    val error: String? = null
)