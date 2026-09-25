package com.guesscountry.game.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guesscountry.game.R
import com.guesscountry.game.data.model.GameResult
import com.guesscountry.game.ui.components.AppTopBar
import com.guesscountry.game.ui.components.GameScreenBackground
import com.guesscountry.game.ui.components.MetricCard
import com.guesscountry.game.ui.components.PrimaryButton
import com.guesscountry.game.ui.components.modeTint
import com.guesscountry.game.ui.components.modeTitle
import com.guesscountry.game.ui.theme.GameColors

@Composable
fun ResultScreen(
    result: GameResult?,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
) {
    GameScreenBackground {
        if (result == null) {
            Column(
                modifier = Modifier.fillMaxSize().safeDrawingPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(stringResource(R.string.loading), color = GameColors.TextMuted)
            }
            return@GameScreenBackground
        }
        val tint = modeTint(result.mode)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .testTag("result_screen"),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { AppTopBar(title = stringResource(R.string.result_title)) }
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .background(tint.copy(alpha = 0.16f), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Outlined.EmojiEvents, contentDescription = null, tint = tint, modifier = Modifier.size(50.dp))
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = when {
                            result.accuracy >= 80 -> "Muhteşem tur!"
                            result.accuracy >= 50 -> "Güzel ilerleme!"
                            else -> "Yeni bir deneme!"
                        },
                        style = MaterialTheme.typography.headlineLarge,
                        color = GameColors.Text,
                    )
                    Spacer(Modifier.height(5.dp))
                    Text("${modeTitle(result.mode)} • ${result.difficulty.displayName}", color = GameColors.TextMuted)
                }
            }
            item {
                ScoreHero(score = result.score, tint = tint)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
                    MetricCard(stringResource(R.string.correct_answers), result.correctAnswers.toString(), GameColors.Mint, Modifier.weight(1f))
                    MetricCard(stringResource(R.string.wrong_answers), result.wrongAnswers.toString(), GameColors.Coral, Modifier.weight(1f))
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
                    MetricCard(stringResource(R.string.accuracy), "%${result.accuracy}", GameColors.Sky, Modifier.weight(1f))
                    MetricCard(stringResource(R.string.longest_combo), "x${result.maxCombo}", GameColors.Amber, Modifier.weight(1f))
                }
            }
            item {
                PrimaryButton(
                    text = stringResource(R.string.play_again),
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxSize(),
                    icon = Icons.Outlined.Refresh,
                )
            }
            item {
                Text(
                    text = stringResource(R.string.go_home),
                    style = MaterialTheme.typography.labelLarge,
                    color = GameColors.TextMuted,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = onHome)
                        .padding(vertical = 10.dp),
                )
            }
        }
    }
}

@Composable
private fun ScoreHero(score: Int, tint: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tint.copy(alpha = 0.13f), RoundedCornerShape(28.dp))
            .padding(22.dp),
    ) {
        Column {
            Text(stringResource(R.string.your_score), style = MaterialTheme.typography.bodyLarge, color = GameColors.TextMuted)
            Spacer(Modifier.height(2.dp))
            Text(
                text = score.toString(),
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 56.sp),
                color = tint,
                fontWeight = FontWeight.Black,
            )
        }
    }
}
