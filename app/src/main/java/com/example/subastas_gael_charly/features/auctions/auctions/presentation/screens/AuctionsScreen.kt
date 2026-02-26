package com.example.subastas_gael_charly.features.auctions.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.subastas_gael_charly.features.auctions.auctions.presentation.components.AuctionItem
import com.example.subastas_gael_charly.features.auctions.auctions.presentation.viewmodels.AuctionsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuctionsScreen(viewModel: AuctionsViewModel) {
    val auctions by viewModel.auctions.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Pilar de Programación Reactiva: Escuchar eventos de error (Rollback)
    LaunchedEffect(Unit) {
        viewModel.errorEvents.collectLatest { errorMessage ->
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Subastas en Tiempo Real") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        if (auctions.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(auctions) { auction ->
                    AuctionItem(
                        auction = auction,
                        onBidClick = { id, newPrice, oldPrice ->
                            viewModel.bid(id, newPrice, oldPrice)
                        }
                    )
                }
            }
        }
    }
}