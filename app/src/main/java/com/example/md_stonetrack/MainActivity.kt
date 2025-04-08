package com.example.md_stonetrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.md_stonetrack.presentation.OrdersScreen.OrdersScreen
import com.example.md_stonetrack.presentation.SignInScreen.AuthViewModel
import com.example.md_stonetrack.presentation.SignInScreen.SignInScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Nav()
        }
    }
}

@Composable
fun Nav() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthUiState.Success) {
            navController.navigate("orders_screen") {
                popUpTo("signin") { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "signin"
    ) {
        composable("signin") {
            SignInScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        composable("orders_screen") {
            OrdersScreen(
                token = (authState as? AuthViewModel.AuthUiState.Success)?.tokens?.accessToken ?: "",
                viewModel = koinViewModel()
            )
        }
    }
}
