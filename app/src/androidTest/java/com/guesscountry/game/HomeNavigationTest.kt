package com.guesscountry.game

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeOpensAndFlagModeCanBeStarted() {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithTag("home_screen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("home_screen").assertIsDisplayed()
        composeRule.onNodeWithTag("play_button").performClick()
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithTag("mode_selection").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("select_flag").performClick()
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithTag("difficulty_selection").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("difficulty_easy").performScrollTo().performClick()
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithTag("game_screen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("game_screen").assertIsDisplayed()
        assert(composeRule.onAllNodesWithTag("answer_option_0").fetchSemanticsNodes().size == 1)
        composeRule.onNodeWithTag("answer_option_0").performClick()
    }
}
