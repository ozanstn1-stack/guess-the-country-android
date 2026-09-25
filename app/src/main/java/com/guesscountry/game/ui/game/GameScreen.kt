package com.guesscountry.game.ui.game

import android.media.AudioManager
import android.media.ToneGenerator
import android.view.HapticFeedbackConstants
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guesscountry.game.R
import com.guesscountry.game.ui.components.AnswerButton
import com.guesscountry.game.ui.components.AppTopBar
import com.guesscountry.game.ui.components.ConfettiBurst
import com.guesscountry.game.ui.components.FeedbackBanner
import com.guesscountry.game.ui.components.GameScreenBackground
import com.guesscountry.game.ui.components.QuestionVisual
import com.guesscountry.game.ui.components.StatPill
import com.guesscountry.game.ui.theme.GameColors

@Composable
fun GameScreen(
    state: GameUiState,
    onAnswer: (String) -> Unit,
    onBack: () -> Unit,
    onRetry: () -> Unit,
) {
    val view = LocalView.current
    val toneGenerator = remember {
        runCatching { ToneGenerator(AudioManager.STREAM_MUSIC, 65) }.getOrNull()
    }
    DisposableEffect(toneGenerator) {
        onDispose { toneGenerator?.release() }
    }
    LaunchedEffect(state.feedback?.status, state.feedback?.selectedAnswer) {
        val feedback = state.feedback ?: return@LaunchedEffect
        if (feedback.vibrationEnabled) {
            view.performHapticFeedback(
                if (feedback.status == AnswerStatus.CORRECT) {
                    HapticFeedbackConstants.LONG_PRESS
                } else {
                    HapticFeedbackConstants.KEYBOARD_TAP
                },
            )
        }
        if (feedback.soundEnabled) {
            toneGenerator?.startTone(
                if (feedback.status == AnswerStatus.CORRECT) {
                    ToneGenerator.TONE_PROP_BEEP
                } else {
                    ToneGenerator.TONE_PROP_NACK
                },
                120,
            )
        }
    }

    GameScreenBackground {
        if (state.isLoading) {
            LoadingContent()
            return@GameScreenBackground
        }
        if (state.errorMessage != null) {
            ErrorContent(state.errorMessage, onRetry, onBack)
            return@GameScreenBackground
        }
        val question = state.currentQuestion ?: return@GameScreenBackground
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .testTag("game_screen"),
            ) {
                AppTopBar(
                    title = if (state.config.isDaily) "DAILY CHALLENGE" else "WORLD PASS",
                    onBack = onBack,
                    trailing = {
                        if (state.config.isDaily) {
                            Text("10 SORU", style = MaterialTheme.typography.labelMedium, color = GameColors.Amber)
                        }
                    },
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    StatPill(
                        label = stringResource(R.string.question_of, state.questionNumber, state.totalQuestions),
                        value = "",
                        tint = GameColors.Sky,
                        modifier = Modifier.weight(1f),
                    )
                    StatPill(
                        label = stringResource(R.string.score),
                        value = state.score.toString(),
                        tint = GameColors.Mint,
                        modifier = Modifier.weight(0.8f),
                    )
                    StatPill(
                        label = stringResource(R.string.combo),
                        value = "x${state.combo}",
                        tint = GameColors.Amber,
                        modifier = Modifier.weight(0.65f),
                    )
                }
                Spacer(Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = state.progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp),
                    color = GameColors.Mint,
                    trackColor = GameColors.SurfaceMuted,
                )
                Spacer(Modifier.height(20.dp))
                Crossfade(
                    targetState = question.id,
                    animationSpec = tween(260),
                    label = "questionTransition",
                ) { questionId ->
                    val displayedQuestion = state.questions.firstOrNull { it.id == questionId } ?: question
                    Column {
                        QuestionVisual(
                            type = displayedQuestion.type,
                            label = displayedQuestion.visualLabel,
                        )
                        Spacer(Modifier.height(20.dp))
                        Text(
                            text = displayedQuestion.questionText,
                            style = MaterialTheme.typography.headlineMedium,
                            color = GameColors.Text,
                        )
                        Spacer(Modifier.height(18.dp))
                        displayedQuestion.options.forEachIndexed { index, option ->
                            AnswerButton(
                                text = option,
                                index = index,
                                selected = state.selectedAnswer == option,
                                correct = option == displayedQuestion.correctAnswer,
                                answered = state.isAnswered,
                                onClick = { onAnswer(option) },
                            )
                            if (index != displayedQuestion.options.lastIndex) Spacer(Modifier.height(10.dp))
                        }
                    }
                }
                if (state.feedback != null) {
                    Spacer(Modifier.height(14.dp))
                    FeedbackBanner(
                        status = state.feedback.status,
                        gainedPoints = state.feedback.gainedPoints,
                    )
                }
                Spacer(Modifier.height(20.dp))
            }
            if (state.feedback?.status == AnswerStatus.CORRECT) {
                ConfettiBurst(visible = true)
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = GameColors.Mint)
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.loading), color = GameColors.TextMuted)
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Outlined.Close, contentDescription = null, tint = GameColors.Error, modifier = Modifier.size(42.dp))
        Spacer(Modifier.height(12.dp))
        Text(message, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(18.dp))
        Text("Tekrar dene", color = GameColors.Mint, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onRetry))
        Spacer(Modifier.height(10.dp))
        Text("Ana menüye dön", color = GameColors.TextMuted, modifier = Modifier.clickable(onClick = onBack))
    }
}
