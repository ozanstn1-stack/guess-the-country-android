package com.guesscountry.game.data.model

enum class Difficulty(val key: String, val displayName: String) {
    EASY("easy", "Kolay"),
    MEDIUM("medium", "Orta"),
    HARD("hard", "Zor");

    companion object {
        fun fromKey(value: String): Difficulty = entries.firstOrNull { it.key == value } ?: EASY
    }
}
