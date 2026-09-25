package com.guesscountry.game.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.guesscountry.game.R
import com.guesscountry.game.data.model.PlayerProgress
import com.guesscountry.game.ui.components.AppTopBar
import com.guesscountry.game.ui.components.GameScreenBackground
import com.guesscountry.game.ui.theme.GameColors

@Composable
fun SettingsScreen(
    progress: PlayerProgress,
    onSoundChanged: (Boolean) -> Unit,
    onVibrationChanged: (Boolean) -> Unit,
    onBack: () -> Unit,
) {
    GameScreenBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .testTag("settings_screen"),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item { AppTopBar(title = stringResource(R.string.settings_title), onBack = onBack) }
            item {
                Column {
                    Text(stringResource(R.string.settings_title), style = MaterialTheme.typography.headlineLarge)
                    Text(stringResource(R.string.settings_subtitle), style = MaterialTheme.typography.bodyLarge, color = GameColors.TextMuted)
                }
            }
            item {
                SettingToggle(
                    icon = Icons.Outlined.GraphicEq,
                    title = stringResource(R.string.sound),
                    subtitle = "Doğru ve yanlış cevap geri bildirimi",
                    checked = progress.soundEnabled,
                    onCheckedChange = onSoundChanged,
                )
            }
            item {
                SettingToggle(
                    icon = Icons.Outlined.Vibration,
                    title = stringResource(R.string.vibration),
                    subtitle = "Cevap seçiminde kısa dokunsal geri bildirim",
                    checked = progress.vibrationEnabled,
                    onCheckedChange = onVibrationChanged,
                )
            }
            item {
                Text(
                    text = "Veriler yalnızca bu cihazda saklanır. Uygulama internet izni istemez.",
                    style = MaterialTheme.typography.bodySmall,
                    color = GameColors.TextMuted,
                    modifier = Modifier.padding(top = 14.dp),
                )
            }
        }
    }
}

@Composable
private fun SettingToggle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = GameColors.Mint, modifier = Modifier.size(26.dp))
        Column(modifier = Modifier.weight(1f).padding(horizontal = 14.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = GameColors.TextMuted)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = GameColors.Ink,
                checkedTrackColor = GameColors.Mint,
                uncheckedThumbColor = GameColors.TextMuted,
                uncheckedTrackColor = GameColors.SurfaceMuted,
                uncheckedBorderColor = GameColors.Outline,
            ),
        )
    }
}
