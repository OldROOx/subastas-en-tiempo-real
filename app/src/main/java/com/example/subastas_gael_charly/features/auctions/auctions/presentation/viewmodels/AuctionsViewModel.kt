package com.example.subastas_gael_charly.features.auctions.auctions.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import com.example.subastas_gael_charly.features.auctions.auctions.domain.usescases.PlaceBidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuctionsViewModel @Inject constructor(
    private val repository: AuctionRepository,
    private val placeBidUseCase: PlaceBidUseCase
) : ViewModel() {

    val auctions = repository.getAuctionsStream()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _errorEvents = MutableSharedFlow<String>()
    val errorEvents = _errorEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.refreshAuctions()
        }
        repository.startRealTimeUpdates()
    }

    fun bid(auctionId: Int, newPrice: Double, currentPrice: Double) {
        viewModelScope.launch {
            val result = placeBidUseCase(auctionId, newPrice, currentPrice)
            if (result.isFailure) {
                _errorEvents.emit("La puja falló. El precio fue restaurado.")
            }
        }
    }
}