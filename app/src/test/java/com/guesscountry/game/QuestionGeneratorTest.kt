package com.guesscountry.game.domain.generator

import com.guesscountry.game.data.model.Country
import com.guesscountry.game.data.model.Difficulty
import com.guesscountry.game.data.model.QuestionType
import com.guesscountry.game.domain.model.GameSessionConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionGeneratorTest {
    private val countries = (1..20).map { index ->
        Country(
            countryName = "Country $index",
            countryCode = "A${('A'.code + index).toChar()}",
            capital = "Capital $index",
            continent = if (index % 2 == 0) "Europe" else "Asia",
            currency = "Currency $index",
            currencyCode = "CU$index",
            flag = "🏳️",
            famousPlaces = listOf("Landmark $index", "Museum $index"),
            difficulty = if (index <= 8) "easy" else "medium",
            countryNameTr = "Ülke $index",
        )
    }

    @Test
    fun generatesTenUniqueQuestionsWithFourOptions() {
        val questions = QuestionGenerator(countries).generate(
            config = GameSessionConfig(QuestionType.FLAG, Difficulty.EASY),
            randomSeed = 42L,
        )

        assertEquals(10, questions.size)
        assertEquals(10, questions.map { it.id }.toSet().size)
        questions.forEach { question ->
            assertEquals(4, question.options.size)
            assertTrue(question.correctAnswer in question.options)
            assertEquals(4, question.options.toSet().size)
        }
    }

    @Test
    fun deterministicSeedProducesSameQuestionSet() {
        val generator = QuestionGenerator(countries)
        val config = GameSessionConfig(QuestionType.CAPITAL, Difficulty.MEDIUM)
        val first = generator.generate(config, randomSeed = 20260925L)
        val second = generator.generate(config, randomSeed = 20260925L)

        assertEquals(first.map { it.id }, second.map { it.id })
        assertEquals(first.map { it.correctAnswer }, second.map { it.correctAnswer })
    }

    @Test
    fun usedQuestionIdsAreExcluded() {
        val generator = QuestionGenerator(countries)
        val config = GameSessionConfig(QuestionType.FAMOUS_PLACE, Difficulty.MEDIUM)
        val first = generator.generate(config, count = 5, randomSeed = 7L)
        val second = generator.generate(config, count = 5, usedQuestionIds = first.map { it.id }.toSet(), randomSeed = 8L)

        assertTrue(first.map { it.id }.toSet().intersect(second.map { it.id }.toSet()).isEmpty())
    }

    @Test
    fun everyModeCanProduceQuestions() {
        val generator = QuestionGenerator(countries)
        QuestionType.entries.forEach { type ->
            val questions = generator.generate(
                config = GameSessionConfig(type, Difficulty.MEDIUM),
                count = 10,
                randomSeed = type.ordinal.toLong(),
            )
            assertEquals(type.name, 10, questions.size)
        }
    }
}
