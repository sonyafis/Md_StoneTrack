package com.example.md_stonetrack

import MdStoneTrackTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.md_stonetrack.presentation.OrdersScreen.OrderView
import com.example.md_stonetrack.presentation.SignInScreen.SignInViewModel
import com.example.md_stonetrack.presentation.SignInScreen.SignInView
import com.example.mdstonetrack.presentation.StartScreen.StartView.StartView
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MdStoneTrackTheme {
                Nav()
            }
        }
    }
}

@Composable
fun Nav() {
    val navController = rememberNavController()
    val authViewModel: SignInViewModel = koinViewModel()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        when (authState) {
            is SignInViewModel.AuthUiState.Success -> {
                navController.navigate("orders_screen") {
                    popUpTo("start_screen") { inclusive = true }
                }
            }
            else -> {}
        }
    }

    NavHost(
        navController = navController,
        startDestination = "start_screen" // Устанавливаем стартовый экран
    ) {
        // Стартовый экран
        composable("start_screen") {
            StartView(
                onNavigateToLogin = { navController.navigate("signin") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        // Экран входа
        composable("signin") {
            SignInView(
                viewModel = authViewModel,
                navController = navController
            )
        }

        // Экран регистрации (заглушка)
        composable("register") {
            // TODO: Реализовать экран регистрации
            Text("Register Screen")
        }

        // Экран заказов
        composable("orders_screen") {
            OrderView(
                token = (authState as? SignInViewModel.AuthUiState.Success)?.tokens?.accessToken ?: "",
            )
        }
    }
}