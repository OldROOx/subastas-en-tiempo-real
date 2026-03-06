package com.example.subastas_gael_charly.features.bids.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.subastas_gael_charly.features.bids.presentation.viewmodels.BidsViewModel

@Composable
fun BidsScreen(viewModel: BidsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = state.currentInput,
            onValueChange = viewModel::onAmountChange,
            label = { Text("Monto") }
        )

        Button(onClick = { viewModel.makeBid() }) {
            Text("Pujar")
        }

        LazyColumn {
            items(state.bids) { bid ->
                Text("Monto:${bid.amount}")
            }
        }
    }
}