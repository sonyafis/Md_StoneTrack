package com.fisun.md_stonetrack.presentation.register_screen.navigationTest

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.test.core.app.ApplicationProvider
import com.fisun.md_stonetrack.presentation.register_screen.RegistrationView
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RegistrationNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clickLoginLink_NavigatesToSignIn() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        ).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }

        composeTestRule.setContent {
            TestAppNavHost(navController)
        }

        composeTestRule.onNodeWithTag("loginButton").performClick()

        assertEquals("register", navController.currentDestination?.route)
    }

    @Composable
    private fun TestAppNavHost(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = "register"
        ) {
            composable("register") { RegistrationView(navController = navController) }
            composable("signin") {  }
        }
    }
}
