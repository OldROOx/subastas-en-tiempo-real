package com.example.subastas_gael_charly.features.auctions.auctions.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.subastas_gael_charly.features.auctions.auctions.domain.entities.Auction

@Composable
fun AuctionItem(
    auction: Auction,
    currentUserId: Int?,
    onBidClick: (Int, Int?, Double, Double) -> Unit
) {
    val isOwner = currentUserId != null && auction.userId == currentUserId

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = auction.title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Precio Actual: $${auction.currentPrice}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (isOwner) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⭐ Tu subasta",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    onBidClick(auction.id, auction.userId, auction.currentPrice + 10.0, auction.currentPrice)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = auction.status && !isOwner
            ) {
                Text(if (isOwner) "Es tu subasta" else if (!auction.status) "Cerrada" else "Pujar +$10.00")
            }
        }
    }
}