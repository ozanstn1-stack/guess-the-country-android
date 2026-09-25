package com.guesscountry.game.ui.difficulty

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.guesscountry.game.R
import com.guesscountry.game.data.model.Difficulty
import com.guesscountry.game.data.model.PlayerProgress
import com.guesscountry.game.data.model.QuestionType
import com.guesscountry.game.ui.components.AppTopBar
import com.guesscountry.game.ui.components.GameScreenBackground
import com.guesscountry.game.ui.components.modeTint
import com.guesscountry.game.ui.components.modeTitle
import com.guesscountry.game.ui.theme.GameColors

@Composable
fun DifficultySelectionScreen(
    mode: QuestionType,
    progress: PlayerProgress,
    onBack: () -> Unit,
    onDifficultySelected: (Difficulty) -> Unit,
) {
    val tint = modeTint(mode)
    GameScreenBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .testTag("difficulty_selection"),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                AppTopBar(title = modeTitle(mode), onBack = onBack)
            }
            item {
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(stringResource(R.string.difficulty_title), style = MaterialTheme.typography.headlineLarge)
                    Text(stringResource(R.string.difficulty_subtitle), style = MaterialTheme.typography.bodyLarge, color = GameColors.TextMuted)
                }
            }
            items(Difficulty.entries) { difficulty ->
                DifficultyCard(
                    difficulty = difficulty,
                    tint = tint,
                    unlocked = progress.isDifficultyUnlocked(difficulty),
                    onClick = { onDifficultySelected(difficulty) },
                )
            }
            item {
                Text(
                    text = "Orta 20 sorudan, zor 80 sorudan sonra açılır. Böylece ilerleme hissedilir ama oyun kilitlenmez.",
                    style = MaterialTheme.typography.bodySmall,
                    color = GameColors.TextMuted,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun DifficultyCard(
    difficulty: Difficulty,
    tint: Color,
    unlocked: Boolean,
    onClick: () -> Unit,
) {
    val accent = when (difficulty) {
        Difficulty.EASY -> GameColors.Mint
        Difficulty.MEDIUM -> GameColors.Amber
        Difficulty.HARD -> GameColors.Coral
    }
    val description = when (difficulty) {
        Difficulty.EASY -> "Tanıdık ülkeler ve temel bilgiler"
        Difficulty.MEDIUM -> "Daha fazla ülke ve orta zorluk"
        Difficulty.HARD -> "Az bilinen ülkeler ve detaylar"
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .testTag("difficulty_${difficulty.key}")
            .clickable(enabled = unlocked, onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = if (unlocked) GameColors.Surface else GameColors.InkSoft),
        border = BorderStroke(1.dp, if (unlocked) accent.copy(alpha = 0.35f) else GameColors.Outline.copy(alpha = 0.35f)),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(accent.copy(alpha = if (unlocked) 0.15f else 0.07f), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(difficulty.displayName.take(1), style = MaterialTheme.typography.headlineMedium, color = if (unlocked) accent else GameColors.TextMuted)
            }
            Spacer(Modifier.size(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(difficulty.displayName, style = MaterialTheme.typography.titleLarge, color = if (unlocked) GameColors.Text else GameColors.TextMuted)
                    if (!unlocked) {
                        Spacer(Modifier.size(8.dp))
                        Icon(Icons.Outlined.Lock, contentDescription = stringResource(R.string.locked), tint = GameColors.TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(description, style = MaterialTheme.typography.bodyMedium, color = GameColors.TextMuted)
            }
            Text("›", color = if (unlocked) accent else GameColors.TextMuted, style = MaterialTheme.typography.headlineMedium)
        }
    }
}
