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
    private var currentUserId: Int? = null

    fun initAuction(auctionId: Int) {

        if (currentAuctionId == auctionId) return

        currentAuctionId?.let { repository.disconnect() }

        currentAuctionId = auctionId
        repository.connect()
        repository.joinAuction(auctionId)



        _uiState.update {
            it.copy(bids = emptyList())
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(auction = auctionRepository.getAuctionById(auctionId))
            }

            val bids = repository.getBidsByAuction(auctionId)

            _uiState.update {
                it.copy(bids = bids)
            }
        }

        /*
        viewModelScope.launch {
            observeBids().collect { bid ->
                _uiState.update {
                    it.copy(bids = it.bids + bid)
                }
            }
        }
        */
    }

    init {
        repository.connect()
        currentUserId = UserSession.getCurrentUserId()

        viewModelScope.launch {
            observeBids().collect { bid ->
                val updateAuction = _uiState.value.auction
                updateAuction?.currentPrice = bid.amount

                _uiState.update {
                    it.copy(auction = updateAuction)
                }

                _uiState.update {
                    it.copy(bids = it.bids + bid)
                }
            }
        }
    }

    fun onAmountChange(value: String) {
        _uiState.update {
            it.copy(currentInput = value)
        }
    }

    fun makeBid() {
        val auctionId = currentAuctionId ?: return
        val userId = currentUserId?: return
        val amount = _uiState.value.currentInput.toDoubleOrNull()

        if (amount != null) {
            placeBid(auctionId, userId, amount)

            _uiState.update {
                it.copy(currentInput = "")
            }

            val updateAuction = _uiState.value.auction
            updateAuction?.currentPrice = amount

            _uiState.update {
                it.copy(auction = updateAuction)
            }
        }
    }

    override fun onCleared() {
        repository.disconnect()
        super.onCleared()
    }

    fun disconnect() {
        repository.disconnect()
    }
}