package com.example.md_stonetrack

import MdStoneTrackTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.md_stonetrack.presentation.OrderDetailScreen.OrderDetailView
import com.example.md_stonetrack.presentation.OrdersScreen.OrderView
import com.example.md_stonetrack.presentation.ProfileScreen.ProfileView
import com.example.md_stonetrack.presentation.ProfileScreen.ProfileViewModel
import com.example.md_stonetrack.presentation.RegisterScreen.RegistrationView
import com.example.md_stonetrack.presentation.SignInScreen.SignInView
import com.example.md_stonetrack.presentation.SplashScreen.SplashView
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

    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") {
            SplashView(navController)
        }

        // Граф аутентификации
        authGraph(navController)

        // Граф заказов
        ordersGraph(navController)

        // Граф курьера
        courierGraph(navController)
    }
}

// Отдельные графы для разных фич
fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = "start_screen",
        route = "auth_graph"
    ) {
        composable("start_screen") {
            StartView(
                onNavigateToLogin = { navController.navigate("signin") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("signin") {
            SignInView(navController = navController)
        }
        composable("register") {
            RegistrationView(navController = navController)
        }
    }
}

fun NavGraphBuilder.ordersGraph(navController: NavHostController) {
    navigation(
        startDestination = "orders_screen",
        route = "orders_graph"
    ) {
        composable("orders_screen") {
            OrderView(navController)
        }
        composable("order_detail/{id_order}") { backStackEntry ->
            val id_order = backStackEntry.arguments?.getString("id_order")?.toIntOrNull()
            OrderDetailView(id_order = id_order, navController = navController)
        }
        // Новый экран профиля
        composable("profile_screen") {
            val viewModel: ProfileViewModel = koinViewModel()
            ProfileView(
                viewModel = viewModel,
                onLogoutSuccess = {
                    navController.navigate("auth_graph") {
                        popUpTo("orders_graph") { inclusive = true }
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.courierGraph(navController: NavHostController) {
    navigation(
        startDestination = "courier_screen",
        route = "courier_graph"
    ) {
        composable("courier_screen") {
            Text("Экран курьера")
        }
        // Другие экраны графа курьера
    }
}