package com.guesscountry.game

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.guesscountry.game.data.model.Difficulty
import com.guesscountry.game.data.model.GameResult
import com.guesscountry.game.data.model.QuestionType
import com.guesscountry.game.ui.result.ResultScreen
import com.guesscountry.game.ui.theme.GuessCountryTheme
import org.junit.Rule
import org.junit.Test

class ResultScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun completedRoundShowsScoreAndMetrics() {
        composeRule.setContent {
            GuessCountryTheme {
                ResultScreen(
                    result = GameResult(
                        score = 950,
                        correctAnswers = 7,
                        wrongAnswers = 3,
                        accuracy = 70,
                        maxCombo = 5,
                        mode = QuestionType.FLAG,
                        difficulty = Difficulty.MEDIUM,
                    ),
                    onPlayAgain = {},
                    onHome = {},
                )
            }
        }

        composeRule.onNodeWithTag("result_screen").assertIsDisplayed()
        composeRule.onNodeWithText("950").assertIsDisplayed()
        composeRule.onNodeWithText("%70").assertIsDisplayed()
        composeRule.onNodeWithText("x5").assertIsDisplayed()
    }
}
