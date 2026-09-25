package com.guesscountry.game.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import com.guesscountry.game.data.model.AchievementId
import com.guesscountry.game.data.model.CategoryStats
import com.guesscountry.game.data.model.GameResult
import com.guesscountry.game.data.model.PlayerProgress
import com.guesscountry.game.data.model.QuestionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.playerDataStore by preferencesDataStore(name = "player_progress")

class PlayerRepository(private val context: Context) {
    val progress: Flow<PlayerProgress> = context.playerDataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it.toPlayerProgress() }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.playerDataStore.edit { it[Keys.SOUND_ENABLED] = enabled }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.playerDataStore.edit { it[Keys.VIBRATION_ENABLED] = enabled }
    }

    suspend fun recordGame(result: GameResult) {
        context.playerDataStore.edit { preferences ->
            val current = preferences.toPlayerProgress()
            val nextCategory = current.categoryStats[result.mode] ?: CategoryStats()
            val nextDailyDate = current.dailyDateKey
            val dailyScore = if (result.isDaily && result.dateKey != null) {
                if (nextDailyDate == result.dateKey) {
                    maxOf(current.dailyHighScore, result.score)
                } else {
                    result.score
                }
            } else {
                current.dailyHighScore
            }
            val updated = current.copy(
                totalGames = current.totalGames + 1,
                totalQuestions = current.totalQuestions + result.correctAnswers + result.wrongAnswers,
                correctAnswers = current.correctAnswers + result.correctAnswers,
                wrongAnswers = current.wrongAnswers + result.wrongAnswers,
                bestScore = maxOf(current.bestScore, result.score),
                bestCombo = maxOf(current.bestCombo, result.maxCombo),
                dailyHighScore = dailyScore,
                dailyDateKey = if (result.isDaily) result.dateKey ?: nextDailyDate else nextDailyDate,
                playedCountries = current.playedCountries + result.countryCodes,
                categoryStats = current.categoryStats + (
                    result.mode to nextCategory.copy(
                        games = nextCategory.games + 1,
                        questions = nextCategory.questions + result.correctAnswers + result.wrongAnswers,
                        correct = nextCategory.correct + result.correctAnswers,
                    )
                    ),
            )
            val unlocked = updated.unlockedAchievements().map(AchievementId::key).toSet()
            preferences[Keys.SOUND_ENABLED] = updated.soundEnabled
            preferences[Keys.VIBRATION_ENABLED] = updated.vibrationEnabled
            preferences[Keys.TOTAL_GAMES] = updated.totalGames
            preferences[Keys.TOTAL_QUESTIONS] = updated.totalQuestions
            preferences[Keys.CORRECT_ANSWERS] = updated.correctAnswers
            preferences[Keys.WRONG_ANSWERS] = updated.wrongAnswers
            preferences[Keys.BEST_SCORE] = updated.bestScore
            preferences[Keys.BEST_COMBO] = updated.bestCombo
            preferences[Keys.DAILY_HIGH_SCORE] = updated.dailyHighScore
            preferences[Keys.DAILY_DATE_KEY] = updated.dailyDateKey
            preferences[Keys.PLAYED_COUNTRIES] = updated.playedCountries
            preferences[Keys.ACHIEVEMENTS] = unlocked
            QuestionType.entries.forEach { type ->
                val stats = updated.categoryStats[type] ?: CategoryStats()
                val prefix = "category_${type.key}_"
                preferences[intPreferencesKey("${prefix}games")] = stats.games
                preferences[intPreferencesKey("${prefix}questions")] = stats.questions
                preferences[intPreferencesKey("${prefix}correct")] = stats.correct
            }
        }
    }

    private fun Preferences.toPlayerProgress(): PlayerProgress {
        val categoryStats = QuestionType.entries.associateWith { type ->
            CategoryStats(
                games = this[intPreferencesKey("category_${type.key}_games")] ?: 0,
                questions = this[intPreferencesKey("category_${type.key}_questions")] ?: 0,
                correct = this[intPreferencesKey("category_${type.key}_correct")] ?: 0,
            )
        }
        return PlayerProgress(
            soundEnabled = this[Keys.SOUND_ENABLED] ?: true,
            vibrationEnabled = this[Keys.VIBRATION_ENABLED] ?: true,
            totalGames = this[Keys.TOTAL_GAMES] ?: 0,
            totalQuestions = this[Keys.TOTAL_QUESTIONS] ?: 0,
            correctAnswers = this[Keys.CORRECT_ANSWERS] ?: 0,
            wrongAnswers = this[Keys.WRONG_ANSWERS] ?: 0,
            bestScore = this[Keys.BEST_SCORE] ?: 0,
            bestCombo = this[Keys.BEST_COMBO] ?: 0,
            dailyHighScore = this[Keys.DAILY_HIGH_SCORE] ?: 0,
            dailyDateKey = this[Keys.DAILY_DATE_KEY] ?: "",
            playedCountries = this[Keys.PLAYED_COUNTRIES] ?: emptySet(),
            categoryStats = categoryStats,
            achievements = this[Keys.ACHIEVEMENTS] ?: emptySet(),
        )
    }

    private object Keys {
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val TOTAL_GAMES = intPreferencesKey("total_games")
        val TOTAL_QUESTIONS = intPreferencesKey("total_questions")
        val CORRECT_ANSWERS = intPreferencesKey("correct_answers")
        val WRONG_ANSWERS = intPreferencesKey("wrong_answers")
        val BEST_SCORE = intPreferencesKey("best_score")
        val BEST_COMBO = intPreferencesKey("best_combo")
        val DAILY_HIGH_SCORE = intPreferencesKey("daily_high_score")
        val DAILY_DATE_KEY = stringPreferencesKey("daily_date_key")
        val PLAYED_COUNTRIES = stringSetPreferencesKey("played_countries")
        val ACHIEVEMENTS = stringSetPreferencesKey("achievements")
    }
}
