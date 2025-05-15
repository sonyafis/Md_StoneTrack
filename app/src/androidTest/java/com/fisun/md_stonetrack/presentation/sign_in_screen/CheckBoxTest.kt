package com.fisun.md_stonetrack.presentation.sign_in_screen

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class CheckBoxTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun signInScreen_termsCheckbox_toggleWorks() {
        composeTestRule.setContent {
            SignInView(navController = rememberNavController())
        }

        val termsCheckbox = composeTestRule.onNodeWithTag("TermsCheckbox")

        termsCheckbox.assertExists()
        termsCheckbox.assert(SemanticsMatcher.expectValue(SemanticsProperties.ToggleableState, ToggleableState.Off))
        termsCheckbox.performClick()
        termsCheckbox.assert(SemanticsMatcher.expectValue(SemanticsProperties.ToggleableState, ToggleableState.On))
    }
}
