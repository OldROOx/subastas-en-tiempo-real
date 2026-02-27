package com.example.subastas_gael_charly.features.bids.presentation.screens

import com.example.subastas_gael_charly.features.bids.domain.entities.Bid

data class BidsUIState(
    val bids: List<Bid> = emptyList(),
    val currentInput: String = "",
    val isConnected: Boolean = false,
    val error: String? = null
)