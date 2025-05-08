package com.fisun.md_stonetrack.presentation.splash_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue

@Composable
fun SplashView(
    navController: NavHostController,
    viewModel: SplashViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is SplashViewModel.AuthCheckState.Loading -> Unit // Ждем
            is SplashViewModel.AuthCheckState.Authorized -> {
                val role = (uiState as SplashViewModel.AuthCheckState.Authorized).role
                when (role.lowercase()) {
                    "courier" -> navController.navigateToCourierScreen()
                    else -> navController.navigateToOrdersScreen()
                }
            }
            is SplashViewModel.AuthCheckState.Unauthorized -> {
                navController.navigateToAuthScreen()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

// Расширения для навигации
fun NavHostController.navigateToAuthScreen() {
    navigate("auth_graph") {
        popUpTo("splash_screen") { inclusive = true }
    }
}

fun NavHostController.navigateToOrdersScreen() {
    navigate("orders_graph") {
        popUpTo("splash_screen") { inclusive = true }
    }
}

fun NavHostController.navigateToCourierScreen() {
    navigate("courier_graph") {
        popUpTo("splash_screen") { inclusive = true }
    }
}