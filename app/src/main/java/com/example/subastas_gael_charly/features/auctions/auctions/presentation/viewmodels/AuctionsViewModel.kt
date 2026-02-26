package com.example.subastas_gael_charly.features.auctions.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subastas_gael_charly.features.auctions.domain.usescases.PlaceBidUseCase
import com.example.subastas_gael_charly.features.auctions.domain.repositories.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuctionsViewModel @Inject constructor(
    private val repository: AuctionRepository,
    private val placeBidUseCase: PlaceBidUseCase
) : ViewModel() {

    // UI observa este StateFlow que viene de la DB
    val auctions = repository.getAuctionsStream()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // SharedFlow para errores (Pilar de la actividad)
    private val _errorEvents = MutableSharedFlow<String>()
    val errorEvents = _errorEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.refreshAuctions() // Carga inicial
            (repository as AuctionRepositoryImpl).startRealTimeUpdates() // Inicia tiempo real
        }
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