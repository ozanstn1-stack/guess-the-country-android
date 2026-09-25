package com.guesscountry.game.data.model

data class Question(
    val id: String,
    val type: QuestionType,
    val countryCode: String,
    val questionText: String,
    val correctAnswer: String,
    val options: List<String>,
    val fact: String = "",
    val visualLabel: String = "",
)
