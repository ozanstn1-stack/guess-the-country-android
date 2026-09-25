package com.guesscountry.game.ui.mode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.guesscountry.game.R
import com.guesscountry.game.data.model.QuestionType
import com.guesscountry.game.ui.components.AppTopBar
import com.guesscountry.game.ui.components.GameScreenBackground
import com.guesscountry.game.ui.components.ModeCard
import com.guesscountry.game.ui.components.modeSubtitle
import com.guesscountry.game.ui.components.modeTint
import com.guesscountry.game.ui.components.modeTitle
import com.guesscountry.game.ui.theme.GameColors

@Composable
fun ModeSelectionScreen(
    onBack: () -> Unit,
    onModeSelected: (QuestionType) -> Unit,
) {
    GameScreenBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .testTag("mode_selection"),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                AppTopBar(title = stringResource(R.string.all_modes), onBack = onBack)
            }
            item {
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text("Modunu seç", style = MaterialTheme.typography.headlineLarge, color = GameColors.Text)
                    Text("Her tur 10 sorudan oluşur.", style = MaterialTheme.typography.bodyLarge, color = GameColors.TextMuted)
                }
            }
            items(QuestionType.entries) { type ->
                ModeCard(
                    type = type,
                    title = modeTitle(type),
                    subtitle = modeSubtitle(type),
                    tint = modeTint(type),
                    onClick = { onModeSelected(type) },
                    modifier = Modifier.testTag("select_${type.key}"),
                )
            }
        }
    }
}
