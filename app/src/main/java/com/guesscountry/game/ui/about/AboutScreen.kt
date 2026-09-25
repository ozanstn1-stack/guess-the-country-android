package com.guesscountry.game.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.guesscountry.game.R
import com.guesscountry.game.ui.components.AppTopBar
import com.guesscountry.game.ui.components.GameScreenBackground
import com.guesscountry.game.ui.components.OfflineBadge
import com.guesscountry.game.ui.theme.GameColors

@Composable
fun AboutScreen(onBack: () -> Unit) {
    GameScreenBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .testTag("about_screen"),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item { AppTopBar(title = stringResource(R.string.about), onBack = onBack) }
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                    Text("GC", style = MaterialTheme.typography.displayLarge, color = GameColors.Mint)
                    Text(stringResource(R.string.about_title), style = MaterialTheme.typography.headlineMedium, color = GameColors.Text)
                    Spacer(Modifier.height(8.dp))
                    OfflineBadge()
                }
            }
            item {
                Text(
                    text = stringResource(R.string.about_body),
                    style = MaterialTheme.typography.bodyLarge,
                    color = GameColors.TextMuted,
                    textAlign = TextAlign.Center,
                )
            }
            item {
                AboutFeature(Icons.Outlined.CloudOff, "Offline çalışır", "İnternet bağlantısı olmadan oyna.")
            }
            item {
                AboutFeature(Icons.Outlined.Lock, "Veriler cihazında", "Puan ve istatistikler cihazda saklanır.")
            }
            item {
                AboutFeature(Icons.Outlined.Language, "Çok dilli mimari", "Yeni diller ve sorular kolayca eklenebilir.")
            }
            item {
                Text("v1.0.0", style = MaterialTheme.typography.labelMedium, color = GameColors.TextMuted, modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun AboutFeature(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    body: String,
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = GameColors.Mint, modifier = Modifier.size(28.dp))
        Column(modifier = Modifier.padding(start = 14.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(body, style = MaterialTheme.typography.bodySmall, color = GameColors.TextMuted)
        }
    }
}
