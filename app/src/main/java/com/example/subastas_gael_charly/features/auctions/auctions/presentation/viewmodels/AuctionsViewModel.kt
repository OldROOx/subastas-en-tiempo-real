package com.example.subastas_gael_charly.features.auctions.auctions.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subastas_gael_charly.core.session.UserSession
import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import com.example.subastas_gael_charly.features.auctions.auctions.domain.usescases.CreateAuctionUseCase
import com.example.subastas_gael_charly.features.auctions.auctions.domain.usescases.PlaceBidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuctionsViewModel @Inject constructor(
    private val repository: AuctionRepository,
    private val placeBidUseCase: PlaceBidUseCase,
    private val createAuctionUseCase: CreateAuctionUseCase
) : ViewModel() {

    val auctions = repository.getAuctionsStream()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val currentUserId: Int? get() = UserSession.getCurrentUserId()

    private val _errorEvents = MutableSharedFlow<String>()
    val errorEvents = _errorEvents.asSharedFlow()

    private val _successEvents = MutableSharedFlow<String>()
    val successEvents = _successEvents.asSharedFlow()

    init {
        viewModelScope.launch { repository.refreshAuctions() }
        repository.startRealTimeUpdates()
    }

    fun bid(auctionId: Int, auctionOwnerId: Int?, newPrice: Double, currentPrice: Double) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            val result = placeBidUseCase(
                auctionId = auctionId,
                auctionOwnerId = auctionOwnerId,
                currentUserId = userId,
                newAmount = newPrice,
                oldAmount = currentPrice
            )
            if (result.isFailure) {
                _errorEvents.emit(result.exceptionOrNull()?.message ?: "La puja falló")
            } else {
                _successEvents.emit("¡Puja realizada!")
                repository.refreshAuctions()
            }
        }
    }

    fun createAuction(title: String, price: Double, endTime: String) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            val result = createAuctionUseCase(title, price, endTime, userId)
            if (result.isSuccess) {
                _successEvents.emit("Subasta creada correctamente")
                repository.refreshAuctions()
            } else {
                _errorEvents.emit(result.exceptionOrNull()?.message ?: "Error al crear subasta")
            }
        }
    }
}