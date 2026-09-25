package com.guesscountry.game.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val countryName: String,
    val countryCode: String,
    val capital: String = "",
    val continent: String = "",
    val currency: String = "",
    val currencyCode: String = "",
    val flag: String = "",
    val mapImage: String = "",
    val famousPlaces: List<String> = emptyList(),
    val difficulty: String = "medium",
    val alternativeNames: List<String> = emptyList(),
    val countryNameTr: String = countryName,
) {
    val difficultyLevel: Difficulty
        get() = Difficulty.fromKey(difficulty)

    fun displayName(): String = countryNameTr.ifBlank { countryName }
}
