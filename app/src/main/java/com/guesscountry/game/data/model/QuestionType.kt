package com.guesscountry.game.data.model

enum class QuestionType(val key: String) {
    FLAG("flag"),
    CAPITAL("capital"),
    MAP("map"),
    FAMOUS_PLACE("famous_place"),
    CURRENCY("currency");

    companion object {
        fun fromKey(value: String): QuestionType = entries.firstOrNull { it.key == value } ?: FLAG
    }
}
