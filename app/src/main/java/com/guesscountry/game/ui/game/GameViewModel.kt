package com.guesscountry.game.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.guesscountry.game.data.local.PlayerRepository
import com.guesscountry.game.data.model.Difficulty
import com.guesscountry.game.data.model.GameResult
import com.guesscountry.game.data.model.Question
import com.guesscountry.game.data.model.QuestionType
import com.guesscountry.game.data.repository.CountryRepository
import com.guesscountry.game.domain.generator.QuestionGenerator
import com.guesscountry.game.domain.model.GameSessionConfig
import com.guesscountry.game.domain.usecase.GameRules
import java.time.LocalDate
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AnswerStatus {
    IDLE,
    CORRECT,
    INCORRECT
}

data class AnswerFeedback(
    val status: AnswerStatus,
    val selectedAnswer: String,
    val correctAnswer: String,
    val gainedPoints: Int,
    val soundEnabled: Boolean,
    val vibrationEnabled: Boolean,
)

data class GameUiState(
    val config: GameSessionConfig,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val score: Int = 0,
    val combo: Int = 0,
    val maxCombo: Int = 0,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val selectedAnswer: String? = null,
    val isAnswered: Boolean = false,
    val feedback: AnswerFeedback? = null,
    val result: GameResult? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentIndex)

    val questionNumber: Int
        get() = (currentIndex + 1).coerceAtMost(questions.size.coerceAtLeast(1))

    val totalQuestions: Int
        get() = questions.size

    val progress: Float
        get() = if (questions.isEmpty()) 0f else currentIndex.toFloat() / questions.size
}

class GameViewModel(
    private val countryRepository: CountryRepository,
    private val playerRepository: PlayerRepository,
    config: GameSessionConfig = GameSessionConfig(QuestionType.FLAG, Difficulty.EASY),
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState(config = config))
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var config = config
    private var countries = emptyList<com.guesscountry.game.data.model.Country>()

    init {
        start()
    }

    fun configure(newConfig: GameSessionConfig) {
        if (newConfig == config && _uiState.value.questions.isNotEmpty()) return
        config = newConfig
        start()
    }

    fun start() {
        _uiState.value = GameUiState(config = config, isLoading = true)
        viewModelScope.launch {
            runCatching { countryRepository.getCountries() }
                .onSuccess { loadedCountries ->
                    countries = loadedCountries
                    val questions = QuestionGenerator(countries).generate(
                        config = config,
                        randomSeed = config.dailySeed ?: Random.nextLong(),
                    )
                    if (questions.isEmpty()) {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = "Bu mod için soru bulunamadı.")
                        }
                    } else {
                        _uiState.update { it.copy(questions = questions, isLoading = false) }
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Sorular yüklenemedi.",
                        )
                    }
                }
        }
    }

    fun submitAnswer(answer: String) {
        val state = _uiState.value
        val question = state.currentQuestion ?: return
        if (state.isAnswered) return
        val correct = answer == question.correctAnswer
        val nextCombo = GameRules.nextCombo(state.combo, correct)
        val gained = if (correct) GameRules.pointsForCombo(nextCombo) else 0
        _uiState.update {
            it.copy(
                selectedAnswer = answer,
                isAnswered = true,
                score = it.score + gained,
                combo = nextCombo,
                maxCombo = maxOf(it.maxCombo, nextCombo),
                correctAnswers = it.correctAnswers + if (correct) 1 else 0,
                wrongAnswers = it.wrongAnswers + if (correct) 0 else 1,
                feedback = AnswerFeedback(
                    status = if (correct) AnswerStatus.CORRECT else AnswerStatus.INCORRECT,
                    selectedAnswer = answer,
                    correctAnswer = question.correctAnswer,
                    gainedPoints = gained,
                    soundEnabled = true,
                    vibrationEnabled = true,
                ),
            )
        }
        viewModelScope.launch {
            val settings = playerRepository.progress.first()
            _uiState.update { current ->
                if (current.selectedAnswer == answer && current.isAnswered) {
                    current.copy(
                        feedback = current.feedback?.copy(
                            soundEnabled = settings.soundEnabled,
                            vibrationEnabled = settings.vibrationEnabled,
                        ),
                    )
                } else {
                    current
                }
            }
            delay(if (correct) 700L else 1_100L)
            val latest = _uiState.value
            if (latest.currentIndex >= latest.questions.lastIndex) {
                val result = GameResult(
                    score = latest.score,
                    correctAnswers = latest.correctAnswers,
                    wrongAnswers = latest.wrongAnswers,
                    accuracy = GameRules.accuracy(latest.correctAnswers, latest.questions.size),
                    maxCombo = latest.maxCombo,
                    mode = config.mode,
                    difficulty = config.difficulty,
                    isDaily = config.isDaily,
                    dateKey = if (config.isDaily) LocalDate.now().toString() else null,
                    countryCodes = latest.questions.map { it.countryCode },
                )
                playerRepository.recordGame(result)
                _uiState.update { it.copy(result = result) }
            } else {
                _uiState.update {
                    it.copy(
                        currentIndex = it.currentIndex + 1,
                        selectedAnswer = null,
                        isAnswered = false,
                        feedback = null,
                    )
                }
            }
        }
    }

    fun restart() {
        start()
    }

    fun prepareReplay() {
        _uiState.value = GameUiState(config = config, isLoading = true)
    }

    fun scoreForCombo(combo: Int): Int = GameRules.pointsForCombo(combo)

    class Factory(
        private val countryRepository: CountryRepository,
        private val playerRepository: PlayerRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GameViewModel(countryRepository, playerRepository) as T
        }
    }
}
