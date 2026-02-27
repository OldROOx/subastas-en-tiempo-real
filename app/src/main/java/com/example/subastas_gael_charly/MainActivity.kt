package com.example.subastas_gael_charly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.subastas_gael_charly.core.ui.theme.SubastasgaelcharlyTheme
import com.example.subastas_gael_charly.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SubastasgaelcharlyTheme {
                AppNavigation()
            }
        }
    }
}