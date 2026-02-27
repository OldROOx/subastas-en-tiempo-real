package com.example.subastas_gael_charly.features.bids.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val repository: BidsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BidsUIState())
    val uiState = _uiState.asStateFlow()

    init {
        repository.connect()
        repository.joinAuction(2)

        viewModelScope.launch {
            observeBids().collect { bid ->
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
        val amount = _uiState.value.currentInput.toDoubleOrNull()
        if (amount != null) {
            placeBid(2, 2, amount)

            _uiState.update {
                it.copy(currentInput = "")
            }
        }
    }

    override fun onCleared() {
        repository.disconnect()
        super.onCleared()
    }
}