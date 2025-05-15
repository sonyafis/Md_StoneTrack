package com.fisun.md_stonetrack.presentation.sign_in_screen.navigationTest

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.fisun.md_stonetrack.presentation.sign_in_screen.SignInView
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class SignInNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clickForgotPassword_NavigatesToResetPassword() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        ).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }
        composeTestRule.setContent {
            NavHost(
                navController = navController,
                startDestination = "signin"
            ) {
                composable("signin") {
                    SignInView(navController = navController)
                }
                composable("reset_password") { }
            }
        }
        Assert.assertEquals("signin", navController.currentDestination?.route)
        composeTestRule.onNodeWithTag("resetPasswordButton").performClick()
        Assert.assertEquals("reset_password", navController.currentDestination?.route)
    }
}