package com.guesscountry.game.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.guesscountry.game.data.model.QuestionType
import com.guesscountry.game.ui.components.GameScreenBackground
import com.guesscountry.game.ui.components.ModeCard
import com.guesscountry.game.ui.components.OfflineBadge
import com.guesscountry.game.ui.components.PrimaryButton
import com.guesscountry.game.ui.components.modeSubtitle
import com.guesscountry.game.ui.components.modeTint
import com.guesscountry.game.ui.components.modeTitle
import com.guesscountry.game.ui.theme.GameColors

@Composable
fun HomeScreen(
    onPlay: () -> Unit,
    onDaily: () -> Unit,
    onMode: (QuestionType) -> Unit,
    onStatistics: () -> Unit,
    onAchievements: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit,
) {
    GameScreenBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .testTag("home_screen"),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                RowHeader(onStatistics, onSettings)
            }
            item {
                Column {
                    Text(
                        text = stringResource(R.string.home_title),
                        style = MaterialTheme.typography.displayLarge,
                        color = GameColors.Text,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.home_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        color = GameColors.TextMuted,
                    )
                    Spacer(Modifier.height(14.dp))
                    OfflineBadge()
                }
            }
            item {
                DailyChallengeCard(onClick = onDaily)
            }
            item {
                PrimaryButton(
                    text = stringResource(R.string.play),
                    onClick = onPlay,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("play_button"),
                )
            }
            item {
                SectionTitle("OYUN MODLARI", "Her mod kısa ve hızlı")
            }
            items(QuestionType.entries) { type ->
                ModeCard(
                    type = type,
                    title = modeTitle(type),
                    subtitle = modeSubtitle(type),
                    tint = modeTint(type),
                    onClick = { onMode(type) },
                    modifier = Modifier.testTag("mode_${type.key}"),
                )
            }
            item {
                SectionTitle("HESABIN", "İlerlemen cihazında saklanır")
            }
            item {
                QuickActions(
                    onStatistics = onStatistics,
                    onAchievements = onAchievements,
                    onSettings = onSettings,
                    onAbout = onAbout,
                )
            }
        }
    }
}

@Composable
private fun RowHeader(onStatistics: () -> Unit, onSettings: () -> Unit) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "GC",
            modifier = Modifier
                .size(44.dp)
                .padding(4.dp),
            color = GameColors.Mint,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
        )
        Text(
            text = "WORLD PASS",
            style = MaterialTheme.typography.labelMedium,
            color = GameColors.Mint,
            letterSpacing = 1.8.sp,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onStatistics, modifier = Modifier.size(48.dp)) {
            Icon(Icons.Outlined.BarChart, contentDescription = stringResource(R.string.statistics), tint = GameColors.TextMuted)
        }
        IconButton(onClick = onSettings, modifier = Modifier.size(48.dp)) {
            Icon(Icons.Outlined.Settings, contentDescription = stringResource(R.string.settings), tint = GameColors.TextMuted)
        }
    }
}

@Composable
private fun DailyChallengeCard(onClick: () -> Unit) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 2.dp)
            .background(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(listOf(GameColors.SurfaceRaised, GameColors.Surface)),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
            )
            .clickable(onClick = onClick)
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(54.dp)
                .background(GameColors.Amber.copy(alpha = 0.16f), androidx.compose.foundation.shape.RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text("10", style = MaterialTheme.typography.titleLarge, color = GameColors.Amber)
        }
        androidx.compose.foundation.layout.Spacer(Modifier.size(14.dp))
        Column(Modifier.weight(1f)) {
            Text(stringResource(R.string.daily_challenge), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(3.dp))
            Text(stringResource(R.string.daily_badge), style = MaterialTheme.typography.labelMedium, color = GameColors.Amber)
        }
        Text("›", color = GameColors.Amber, fontSize = 28.sp)
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium, color = GameColors.Text)
        Spacer(Modifier.height(3.dp))
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = GameColors.TextMuted)
    }
}

@Composable
private fun QuickActions(
    onStatistics: () -> Unit,
    onAchievements: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        QuickActionRow(
            icon = Icons.Outlined.BarChart,
            title = stringResource(R.string.statistics),
            tint = GameColors.Sky,
            onClick = onStatistics,
        )
        QuickActionRow(
            icon = Icons.Outlined.EmojiEvents,
            title = stringResource(R.string.achievements),
            tint = GameColors.Amber,
            onClick = onAchievements,
        )
        QuickActionRow(
            icon = Icons.Outlined.Settings,
            title = stringResource(R.string.settings),
            tint = GameColors.Lilac,
            onClick = onSettings,
        )
        QuickActionRow(
            icon = Icons.Outlined.Info,
            title = stringResource(R.string.about),
            tint = GameColors.Coral,
            onClick = onAbout,
        )
    }
}

@Composable
private fun QuickActionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    tint: Color,
    onClick: () -> Unit,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Surface, androidx.compose.foundation.shape.RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = tint)
        Spacer(Modifier.size(12.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        Text("›", color = GameColors.TextMuted, fontSize = 24.sp)
    }
}
