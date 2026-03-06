package com.example.subastas_gael_charly.features.bids.domain.usecases

import com.example.subastas_gael_charly.features.bids.domain.entities.Bid
import com.example.subastas_gael_charly.features.bids.domain.repositories.BidsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBidsUseCase @Inject constructor(
    private val repository: BidsRepository
) {
    operator fun invoke(): Flow<Bid> =
        repository.observeBids()
}