package com.guesscountry.game.domain.usecase

object GameRules {
    fun pointsForCombo(combo: Int): Int = 100 + ((combo - 1).coerceIn(0, 4) * 25)

    fun nextCombo(currentCombo: Int, correct: Boolean): Int = if (correct) currentCombo + 1 else 0

    fun accuracy(correct: Int, total: Int): Int = if (total <= 0) 0 else (correct * 100) / total
}
