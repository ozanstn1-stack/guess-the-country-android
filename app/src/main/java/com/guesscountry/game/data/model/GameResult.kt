package com.guesscountry.game.data.model

data class GameResult(
    val score: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val accuracy: Int,
    val maxCombo: Int,
    val mode: QuestionType,
    val difficulty: Difficulty,
    val isDaily: Boolean = false,
    val dateKey: String? = null,
    val countryCodes: List<String> = emptyList(),
)
