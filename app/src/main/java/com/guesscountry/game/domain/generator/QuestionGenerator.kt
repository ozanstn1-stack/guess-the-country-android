package com.guesscountry.game.domain.generator

import com.guesscountry.game.data.model.Country
import com.guesscountry.game.data.model.Difficulty
import com.guesscountry.game.data.model.Question
import com.guesscountry.game.data.model.QuestionType
import com.guesscountry.game.domain.model.GameSessionConfig
import kotlin.random.Random

class QuestionGenerator(
    private val countries: List<Country>,
) {
    fun generate(
        config: GameSessionConfig,
        count: Int = QUESTIONS_PER_GAME,
        usedQuestionIds: Set<String> = emptySet(),
        randomSeed: Long = Random.nextLong(),
    ): List<Question> {
        val random = Random(randomSeed)
        val eligible = countries.filter { it.matches(config.difficulty) && it.hasData(config.mode) }
        val fallback = countries.filter { it.hasData(config.mode) }
        val pool = if (eligible.size >= count) eligible else fallback
        val seeds = pool.flatMap { country -> seedsFor(country, config.mode) }
            .filterNot { it.id in usedQuestionIds }
            .distinctBy { it.id }
            .shuffled(random)
        return seeds.take(count).map { seed ->
            val country = seed.country
            val wrongAnswers = buildWrongAnswers(seed, pool, random, config.mode)
            Question(
                id = seed.id,
                type = config.mode,
                countryCode = country.countryCode,
                questionText = questionText(country, config.mode, seed.answer, seed.visualLabel),
                correctAnswer = seed.answer,
                options = (listOf(seed.answer) + wrongAnswers).shuffled(random),
                fact = "Doğru cevap: ${seed.answer}.",
                visualLabel = seed.visualLabel,
            )
        }
    }

    private fun seedsFor(country: Country, type: QuestionType): List<QuestionSeed> {
        val name = country.displayName()
        return when (type) {
            QuestionType.FLAG -> listOf(
                QuestionSeed(country, name, country.flag.ifBlank { country.countryCode }, "flag_${country.countryCode}"),
            )
            QuestionType.CAPITAL -> country.capital.takeIf(String::isNotBlank)?.let {
                listOf(QuestionSeed(country, it, country.flag, "capital_${country.countryCode}"))
            }.orEmpty()
            QuestionType.MAP -> listOf(
                QuestionSeed(country, name, country.countryCode, "map_${country.countryCode}"),
            )
            QuestionType.FAMOUS_PLACE -> country.famousPlaces.mapIndexed { index, place ->
                QuestionSeed(country, name, place, "place_${country.countryCode}_$index")
            }
            QuestionType.CURRENCY -> country.currency.takeIf(String::isNotBlank)?.let {
                listOf(QuestionSeed(country, it, country.flag, "currency_${country.countryCode}"))
            }.orEmpty()
        }
    }

    private fun buildWrongAnswers(
        seed: QuestionSeed,
        pool: List<Country>,
        random: Random,
        type: QuestionType,
    ): List<String> {
        val candidates = pool.asSequence()
            .filter { it.countryCode != seed.country.countryCode }
            .map { candidate -> answerFor(candidate, type) }
            .filter { it.isNotBlank() && it != seed.answer }
            .distinct()
            .toList()
        val differentContinent = candidates.filter { candidateName ->
            val country = pool.firstOrNull { answerFor(it, type) == candidateName }
            country?.continent != seed.country.continent
        }
        val ordered = (differentContinent + candidates).distinct().shuffled(random)
        return ordered.take(3)
    }

    private fun answerFor(country: Country, type: QuestionType): String = when (type) {
        QuestionType.FLAG, QuestionType.MAP, QuestionType.FAMOUS_PLACE -> country.displayName()
        QuestionType.CAPITAL -> country.capital
        QuestionType.CURRENCY -> country.currency
    }

    private fun questionText(
        country: Country,
        type: QuestionType,
        answer: String,
        visualLabel: String,
    ): String = when (type) {
        QuestionType.FLAG -> "Bu bayrak hangi ülkeye aittir?"
        QuestionType.CAPITAL -> "${country.displayName()} başkenti hangisidir?"
        QuestionType.MAP -> "Bu ülke hangisidir?"
        QuestionType.FAMOUS_PLACE -> "$visualLabel hangi ülkeye aittir?"
        QuestionType.CURRENCY -> "${country.displayName()} para birimi nedir?"
    }

    private fun Country.matches(difficulty: Difficulty): Boolean = when (difficulty) {
        Difficulty.EASY -> difficultyLevel == Difficulty.EASY
        Difficulty.MEDIUM -> difficultyLevel == Difficulty.EASY || difficultyLevel == Difficulty.MEDIUM
        Difficulty.HARD -> true
    }

    private fun Country.hasData(type: QuestionType): Boolean = when (type) {
        QuestionType.FLAG -> flag.isNotBlank() || countryCode.length == 2
        QuestionType.CAPITAL -> capital.isNotBlank()
        QuestionType.MAP -> countryCode.length == 2
        QuestionType.FAMOUS_PLACE -> famousPlaces.isNotEmpty()
        QuestionType.CURRENCY -> currency.isNotBlank()
    }

    private data class QuestionSeed(
        val country: Country,
        val answer: String,
        val visualLabel: String,
        val id: String,
    )

    companion object {
        const val QUESTIONS_PER_GAME = 10
    }
}
