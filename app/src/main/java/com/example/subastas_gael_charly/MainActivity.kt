package com.example.subastas_gael_charly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.subastas_gael_charly.core.ui.theme.SubastasgaelcharlyTheme
import com.example.subastas_gael_charly.features.auctions.presentation.screens.AuctionsScreen
import com.example.subastas_gael_charly.features.auctions.presentation.viewmodels.AuctionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SubastasTheme {
                val viewModel: AuctionsViewModel = hiltViewModel()
                AuctionsScreen(viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SubastasgaelcharlyTheme {
        Greeting("Android")
    }
}