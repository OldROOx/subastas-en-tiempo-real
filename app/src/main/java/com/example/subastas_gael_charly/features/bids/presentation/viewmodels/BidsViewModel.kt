package com.example.subastas_gael_charly.features.bids.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subastas_gael_charly.core.session.UserSession
import com.example.subastas_gael_charly.features.auctions.auctions.domain.repositories.AuctionRepository
import com.example.subastas_gael_charly.features.bids.domain.entities.Bid
import com.example.subastas_gael_charly.features.bids.domain.repositories.BidsRepository
import com.example.subastas_gael_charly.features.bids.domain.usecases.ObserveBidsUseCase
import com.example.subastas_gael_charly.features.bids.domain.usecases.PlaceBidUseCase
import com.example.subastas_gael_charly.features.bids.presentation.screens.BidsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BidsViewModel @Inject constructor(
    private val observeBids: ObserveBidsUseCase,
    private val placeBid: PlaceBidUseCase,
    private val repository: BidsRepository,
    private val auctionRepository: AuctionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BidsUIState())
    val uiState = _uiState.asStateFlow()

    private var currentAuctionId: Int? = null
    private val currentUserId: Int? get() = UserSession.getCurrentUserId()

    // Precio antes de la puja optimista, para hacer rollback si el servidor no responde
    private var priceBeforeOptimisticUpdate: Double? = null

    init {
        repository.connect()

        viewModelScope.launch {
            observeBids().collect { bid ->
                if (bid.auctionId != currentAuctionId) return@collect

                // Llegó la confirmación real del servidor:
                // - Elimina el placeholder optimista (id = -1)
                // - Agrega la puja real con su id definitivo
                // - Sobreescribe el precio con el valor oficial (corrige si hubo diferencia)
                // - Limpia el precio de rollback porque la operación fue exitosa
                priceBeforeOptimisticUpdate = null

                _uiState.update { state ->
                    state.copy(
                        auction = state.auction?.copy(currentPrice = bid.amount),
                        bids = state.bids.filterNot { it.id == -1 } + bid
                    )
                }
            }
        }
    }

    fun initAuction(auctionId: Int) {
        if (currentAuctionId == auctionId) return
        currentAuctionId = auctionId
        repository.joinAuction(auctionId)
        _uiState.update { it.copy(bids = emptyList(), auction = null, currentInput = "") }

        viewModelScope.launch {
            val auction = runCatching { auctionRepository.getAuctionById(auctionId) }.getOrNull()
            _uiState.update { it.copy(auction = auction) }
            val bids = repository.getBidsByAuction(auctionId)
            _uiState.update { it.copy(bids = bids) }
        }
    }

    fun onAmountChange(value: String) {
        _uiState.update { it.copy(currentInput = value) }
    }

    fun makeBid() {
        val auctionId = currentAuctionId ?: return
        val userId = currentUserId ?: return
        val amount = _uiState.value.currentInput.toDoubleOrNull() ?: return

        // Guardar precio actual por si necesitamos hacer rollback
        priceBeforeOptimisticUpdate = _uiState.value.auction?.currentPrice

        // OPTIMISTIC UPDATE: actualizar UI al instante sin esperar al servidor
        val optimisticBid = Bid(
            id = -1,                  // placeholder, el servidor asignará el id real
            auctionId = auctionId,
            userId = userId,
            amount = amount,
            createdAt = "Enviando..."
        )

        _uiState.update { state ->
            state.copy(
                auction = state.auction?.copy(currentPrice = amount),
                bids = state.bids + optimisticBid,
                currentInput = ""
            )
        }

        // Enviar al servidor — si falla, hacer rollback
        viewModelScope.launch {
            // Pequeño delay para que el socket tenga tiempo de responder
            // Si en X ms no llega "new_bid" de confirmación, revertimos
            kotlinx.coroutines.delay(5000)

            // Si priceBeforeOptimisticUpdate sigue con valor, significa que el servidor
            // no respondió con "new_bid" (el collect lo pone a null al llegar)
            val rollbackPrice = priceBeforeOptimisticUpdate
            if (rollbackPrice != null) {
                priceBeforeOptimisticUpdate = null
                _uiState.update { state ->
                    state.copy(
                        auction = state.auction?.copy(currentPrice = rollbackPrice),
                        bids = state.bids.filterNot { it.id == -1 }
                    )
                }
            }
        }

        placeBid(auctionId, userId, amount)
    }

    fun disconnect() {
        repository.disconnect()
    }

    override fun onCleared() {
        repository.disconnect()
        super.onCleared()
    }
}