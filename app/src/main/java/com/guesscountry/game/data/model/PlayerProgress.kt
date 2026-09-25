package com.guesscountry.game.data.model

data class CategoryStats(
    val games: Int = 0,
    val questions: Int = 0,
    val correct: Int = 0,
) {
    val accuracy: Int
        get() = if (questions == 0) 0 else (correct * 100) / questions
}

data class PlayerProgress(
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val totalGames: Int = 0,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val bestScore: Int = 0,
    val bestCombo: Int = 0,
    val dailyHighScore: Int = 0,
    val dailyDateKey: String = "",
    val playedCountries: Set<String> = emptySet(),
    val categoryStats: Map<QuestionType, CategoryStats> = emptyMap(),
    val achievements: Set<String> = emptySet(),
) {
    val accuracy: Int
        get() = if (totalQuestions == 0) 0 else (correctAnswers * 100) / totalQuestions

    fun isDifficultyUnlocked(difficulty: Difficulty): Boolean = when (difficulty) {
        Difficulty.EASY -> true
        Difficulty.MEDIUM -> totalQuestions >= 20 || totalGames >= 3
        Difficulty.HARD -> totalQuestions >= 80 || totalGames >= 10
    }

    fun unlockedAchievements(): Set<AchievementId> = AchievementId.entries.filterTo(mutableSetOf()) { achievement ->
        when (achievement) {
            AchievementId.FIRST_TEN -> totalQuestions >= achievement.target
            AchievementId.TEN_CORRECT -> correctAnswers >= achievement.target
            AchievementId.COMBO_FIVE -> bestCombo >= achievement.target
            AchievementId.SCORE_1000 -> bestScore >= achievement.target
            AchievementId.COUNTRIES_25 -> playedCountries.size >= achievement.target
            AchievementId.COUNTRIES_50 -> playedCountries.size >= achievement.target
            AchievementId.COUNTRIES_100 -> playedCountries.size >= achievement.target
            AchievementId.GAMES_TEN -> totalGames >= achievement.target
        }
    }
}
