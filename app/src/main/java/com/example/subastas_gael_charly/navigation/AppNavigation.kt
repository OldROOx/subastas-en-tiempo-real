package com.example.subastas_gael_charly.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.subastas_gael_charly.features.auctions.auctions.presentation.screens.AuctionsScreen
import com.example.subastas_gael_charly.features.auctions.auctions.presentation.screens.CreateAuctionScreen
import com.example.subastas_gael_charly.features.auctions.auctions.presentation.viewmodels.AuctionsViewModel
import com.example.subastas_gael_charly.features.auth.presentation.screens.LoginScreen
import com.example.subastas_gael_charly.features.auth.presentation.screens.RegisterScreen
import com.example.subastas_gael_charly.features.auth.presentation.viewmodels.AuthViewModel
import com.example.subastas_gael_charly.features.bids.presentation.screens.BidsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Auctions : Screen("auctions")
    object CreateAuction : Screen("create_auction")
    object Bids : Screen("bids/{auctionId}") {
        fun createRoute(auctionId: Int) = "bids/$auctionId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Auctions.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onGoToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            val viewModel: AuthViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = { navController.popBackStack() },
                onGoToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Auctions.route) {
            val viewModel: AuctionsViewModel = hiltViewModel()

            AuctionsScreen(
                viewModel = viewModel,
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateAuction.route)
                },
                onNavigateToBids = { auctionId ->
                    navController.navigate(
                        Screen.Bids.createRoute(auctionId)
                    )
                }
            )
        }

        composable(Screen.CreateAuction.route) {
            val viewModel: AuctionsViewModel = hiltViewModel()
            CreateAuctionScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Bids.route
        ) { backStackEntry ->

            val auctionId = backStackEntry
                .arguments
                ?.getString("auctionId")
                ?.toIntOrNull() ?: return@composable

            BidsScreen()
        }
    }
}