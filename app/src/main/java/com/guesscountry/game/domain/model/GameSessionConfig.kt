package com.guesscountry.game.domain.model

import com.guesscountry.game.data.model.Difficulty
import com.guesscountry.game.data.model.QuestionType

data class GameSessionConfig(
    val mode: QuestionType,
    val difficulty: Difficulty,
    val dailySeed: Long? = null,
) {
    val isDaily: Boolean
        get() = dailySeed != null
}
