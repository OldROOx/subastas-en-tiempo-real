package com.example.subastas_gael_charly.features.auctions.auctions.presentation.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.subastas_gael_charly.features.auctions.domain.entities.Auction

@Composable
fun AuctionItem(
    auction: Auction,
    onBidClick: (Int, Double, Double) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = auction.title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Precio Actual: $${auction.currentPrice}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Simulamos una puja de +10 pesos
                    onBidClick(auction.id, auction.currentPrice + 10.0, auction.currentPrice)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = auction.status
            ) {
                Text("Pujar +$10.00")
            }
        }
    }
}