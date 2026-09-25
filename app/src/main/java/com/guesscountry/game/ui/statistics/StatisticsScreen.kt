package com.guesscountry.game.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.guesscountry.game.R
import com.guesscountry.game.data.model.PlayerProgress
import com.guesscountry.game.data.model.QuestionType
import com.guesscountry.game.ui.components.AppTopBar
import com.guesscountry.game.ui.components.GameScreenBackground
import com.guesscountry.game.ui.components.MetricCard
import com.guesscountry.game.ui.components.modeTint
import com.guesscountry.game.ui.components.modeTitle
import com.guesscountry.game.ui.theme.GameColors

@Composable
fun StatisticsScreen(
    progress: PlayerProgress,
    onBack: () -> Unit,
) {
    val bestCategory = progress.categoryStats.maxByOrNull { it.value.accuracy }
    GameScreenBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .testTag("statistics_screen"),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { AppTopBar(title = stringResource(R.string.statistics), onBack = onBack) }
            item {
                Column {
                    Text("İlerlemen", style = MaterialTheme.typography.headlineLarge, color = GameColors.Text)
                    Text("Tüm veriler bu cihazda saklanır.", style = MaterialTheme.typography.bodyLarge, color = GameColors.TextMuted)
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
                    MetricCard(stringResource(R.string.total_games), progress.totalGames.toString(), GameColors.Sky, Modifier.weight(1f))
                    MetricCard(stringResource(R.string.total_questions), progress.totalQuestions.toString(), GameColors.Mint, Modifier.weight(1f))
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
                    MetricCard(stringResource(R.string.best_score), progress.bestScore.toString(), GameColors.Amber, Modifier.weight(1f))
                    MetricCard(stringResource(R.string.best_combo), "x${progress.bestCombo}", GameColors.Coral, Modifier.weight(1f))
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
                    MetricCard(stringResource(R.string.accuracy), "%${progress.accuracy}", GameColors.Lilac, Modifier.weight(1f))
                    MetricCard("Günlük en iyi", progress.dailyHighScore.toString(), GameColors.Sky, Modifier.weight(1f))
                }
            }
            item {
                Text(stringResource(R.string.category_stats), style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp))
            }
            items(QuestionType.entries) { type ->
                val stats = progress.categoryStats[type]
                CategoryProgress(type = type, questions = stats?.questions ?: 0, accuracy = stats?.accuracy ?: 0)
            }
            item {
                Text(
                    text = if (bestCategory == null) stringResource(R.string.no_data) else "En iyi kategori: ${modeTitle(bestCategory.key)} • %${bestCategory.value.accuracy}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GameColors.TextMuted,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun CategoryProgress(type: QuestionType, questions: Int, accuracy: Int) {
    val tint = modeTint(type)
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(modeTitle(type), style = MaterialTheme.typography.titleMedium, color = GameColors.Text)
            Text(if (questions == 0) "—" else "%$accuracy", style = MaterialTheme.typography.titleMedium, color = tint)
        }
        Spacer(Modifier.height(7.dp))
        LinearProgressIndicator(
            progress = (accuracy / 100f),
            modifier = Modifier.fillMaxSize().height(8.dp),
            color = tint,
            trackColor = GameColors.SurfaceMuted,
        )
        Spacer(Modifier.height(4.dp))
        Text("$questions soru", style = MaterialTheme.typography.labelMedium, color = GameColors.TextMuted)
    }
}
