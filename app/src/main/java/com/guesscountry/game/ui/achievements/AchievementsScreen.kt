package com.guesscountry.game.ui.achievements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.guesscountry.game.R
import com.guesscountry.game.data.model.AchievementId
import com.guesscountry.game.data.model.PlayerProgress
import com.guesscountry.game.ui.components.AppTopBar
import com.guesscountry.game.ui.components.GameScreenBackground
import com.guesscountry.game.ui.theme.GameColors

@Composable
fun AchievementsScreen(
    progress: PlayerProgress,
    onBack: () -> Unit,
) {
    val unlocked = progress.unlockedAchievements()
    GameScreenBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .testTag("achievements_screen"),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { AppTopBar(title = stringResource(R.string.achievements), onBack = onBack) }
            item {
                Column {
                    Text("Küçük hedefler, büyük yolculuk", style = MaterialTheme.typography.headlineLarge, color = GameColors.Text)
                    Text("${unlocked.size} / ${AchievementId.entries.size} başarım açıldı", style = MaterialTheme.typography.bodyLarge, color = GameColors.TextMuted)
                }
            }
            items(AchievementId.entries) { achievement ->
                AchievementCard(
                    achievement = achievement,
                    unlocked = achievement in unlocked,
                    current = achievement.currentValue(progress),
                )
            }
        }
    }
}

@Composable
private fun AchievementCard(
    achievement: AchievementId,
    unlocked: Boolean,
    current: Int,
) {
    val tint = if (unlocked) GameColors.Mint else GameColors.TextMuted
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = if (unlocked) GameColors.Surface else GameColors.InkSoft),
        border = BorderStroke(1.dp, if (unlocked) GameColors.Mint.copy(alpha = 0.35f) else GameColors.Outline.copy(alpha = 0.3f)),
    ) {
        Row(modifier = Modifier.padding(17.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(achievement.emoji, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.size(13.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(achievement.title, style = MaterialTheme.typography.titleMedium, color = if (unlocked) GameColors.Text else GameColors.TextMuted)
                Spacer(Modifier.height(3.dp))
                Text(achievement.description, style = MaterialTheme.typography.bodySmall, color = GameColors.TextMuted)
                if (!unlocked) {
                    Spacer(Modifier.height(9.dp))
                    LinearProgressIndicator(
                        progress = (current.toFloat() / achievement.target).coerceIn(0f, 1f),
                        modifier = Modifier.fillMaxSize().height(6.dp),
                        color = tint,
                        trackColor = GameColors.SurfaceMuted,
                    )
                }
            }
            Spacer(Modifier.size(10.dp))
            if (unlocked) {
                Text("✓", color = GameColors.Mint, style = MaterialTheme.typography.titleLarge)
            } else {
                Icon(Icons.Outlined.Lock, contentDescription = null, tint = GameColors.TextMuted, modifier = Modifier.size(19.dp))
            }
        }
    }
}

private fun AchievementId.currentValue(progress: PlayerProgress): Int = when (this) {
    AchievementId.FIRST_TEN -> progress.totalQuestions
    AchievementId.TEN_CORRECT -> progress.correctAnswers
    AchievementId.COMBO_FIVE -> progress.bestCombo
    AchievementId.SCORE_1000 -> progress.bestScore
    AchievementId.COUNTRIES_25, AchievementId.COUNTRIES_50, AchievementId.COUNTRIES_100 -> progress.playedCountries.size
    AchievementId.GAMES_TEN -> progress.totalGames
}
