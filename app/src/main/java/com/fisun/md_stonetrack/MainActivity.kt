package com.fisun.md_stonetrack

import com.fisun.md_stonetrack.presentation.theme.MdStoneTrackTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.fisun.md_stonetrack.presentation.client.about_us_screen.AboutUsView
import com.fisun.md_stonetrack.presentation.client.account_settings_screen.AccountSettingsView
import com.fisun.md_stonetrack.presentation.client.change_password.ChangePasswordView
import com.fisun.md_stonetrack.presentation.client.feedback_screen.FeedbackView
import com.fisun.md_stonetrack.presentation.client.history_screen.HistoryView
import com.fisun.md_stonetrack.presentation.client.order_detail_screen.OrderDetailView
import com.fisun.md_stonetrack.presentation.client.order_screen.OrderView
import com.fisun.md_stonetrack.presentation.client.profile_screen.ProfileView
import com.fisun.md_stonetrack.presentation.register_screen.RegistrationView
import com.fisun.md_stonetrack.presentation.sign_in_screen.SignInView
import com.fisun.md_stonetrack.presentation.splash_screen.SplashView
import com.fisun.md_stonetrack.presentation.start_screen.StartView
import com.fisun.md_stonetrack.presentation.client.history_detail_screen.HistoryDetailView
import com.fisun.md_stonetrack.presentation.courier.courier_account_settings_screen.CourierAccountSettingsView
import com.fisun.md_stonetrack.presentation.courier.courier_change_password.CourierChangePasswordView
import com.fisun.md_stonetrack.presentation.courier.courier_feedback_screen.CourierFeedbackView
import com.fisun.md_stonetrack.presentation.courier.courier_history_detail_screen.CourierHistoryDetailView
import com.fisun.md_stonetrack.presentation.courier.courier_history_screen.CourierHistoryView
import com.fisun.md_stonetrack.presentation.courier.courier_order_detail_screen.CourierOrderDetailView
import com.fisun.md_stonetrack.presentation.courier.courier_order_screen.CourierView
import com.fisun.md_stonetrack.presentation.courier.courier_profile_screen.CourierProfileView
import com.fisun.md_stonetrack.presentation.reset_password_screen.ResetPasswordView

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
        authGraph(navController)
        ordersGraph(navController)
        courierGraph(navController)
    }
}

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
        composable("reset_password") {
            ResetPasswordView(navController = navController)
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
        composable("history_screen") {
            HistoryView(navController)
        }
        composable("history_detail/{id_order}") { backStackEntry ->
            val id_order = backStackEntry.arguments?.getString("id_order")?.toIntOrNull()
            HistoryDetailView(id_order = id_order, navController = navController)
        }
        composable("feedback_screen") {
            FeedbackView(navController = navController)
        }
        composable("profile_screen") {
            ProfileView(navController)
        }
        composable("about_us_screen") {
            AboutUsView(navController)
        }
        composable("account_settings_screen") {
            AccountSettingsView(navController)
        }
        composable("change_password_screen") {
            ChangePasswordView(navController)
        }
    }
}

fun NavGraphBuilder.courierGraph(navController: NavHostController) {
    navigation(
        startDestination = "courier_screen",
        route = "courier_graph"
    ) {
        composable("courier_screen") {
            CourierView(navController)
        }
        composable("courier_order_detail/{id_order}") { backStackEntry ->
            val id_order = backStackEntry.arguments?.getString("id_order")?.toIntOrNull()
            CourierOrderDetailView(id_order = id_order, navController = navController)
        }
        composable("courier_history_screen") {
            CourierHistoryView(navController)
        }
        composable("courier_history_detail/{id_order}") { backStackEntry ->
            val id_order = backStackEntry.arguments?.getString("id_order")?.toIntOrNull()
            CourierHistoryDetailView(id_order = id_order, navController = navController)
        }
        composable("courier_feedback_screen") {
            CourierFeedbackView(navController = navController)
        }
        composable("courier_profile_screen") {
            CourierProfileView(navController)
        }
        composable("courier_account_settings_screen") {
            CourierAccountSettingsView(navController)
        }
        composable("courier_change_password_screen") {
            CourierChangePasswordView(navController)
        }
    }
}