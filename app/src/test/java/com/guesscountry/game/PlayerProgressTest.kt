package com.guesscountry.game.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerProgressTest {
    @Test
    fun accuracyUsesAnsweredQuestions() {
        val progress = PlayerProgress(totalQuestions = 20, correctAnswers = 15)
        assertEquals(75, progress.accuracy)
    }

    @Test
    fun difficultyUnlocksRemainAccessible() {
        val progress = PlayerProgress(totalQuestions = 80, totalGames = 10)
        assertTrue(progress.isDifficultyUnlocked(Difficulty.EASY))
        assertTrue(progress.isDifficultyUnlocked(Difficulty.MEDIUM))
        assertTrue(progress.isDifficultyUnlocked(Difficulty.HARD))
        assertFalse(PlayerProgress().isDifficultyUnlocked(Difficulty.HARD))
    }

    @Test
    fun achievementsAreDerivedFromPersistedProgress() {
        val progress = PlayerProgress(
            totalGames = 10,
            totalQuestions = 100,
            correctAnswers = 25,
            bestCombo = 5,
            bestScore = 1200,
            playedCountries = (1..100).map { "C$it" }.toSet(),
        )
        val unlocked = progress.unlockedAchievements()
        assertTrue(AchievementId.FIRST_TEN in unlocked)
        assertTrue(AchievementId.SCORE_1000 in unlocked)
        assertTrue(AchievementId.COUNTRIES_100 in unlocked)
        assertTrue(AchievementId.GAMES_TEN in unlocked)
    }
}
