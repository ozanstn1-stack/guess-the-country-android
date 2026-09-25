package com.guesscountry.game.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guesscountry.game.ui.components.GameScreenBackground
import com.guesscountry.game.ui.theme.GameColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(550)
        onFinished()
    }
    GameScreenBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "GC",
                modifier = Modifier
                    .size(104.dp)
                    .background(GameColors.Mint, androidx.compose.foundation.shape.CircleShape)
                    .padding(top = 25.dp),
                color = GameColors.Ink,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Black,
            )
            Spacer(Modifier.height(22.dp))
            Text(
                text = "GUESS THE COUNTRY",
                style = MaterialTheme.typography.titleLarge,
                color = GameColors.Text,
                letterSpacing = 2.sp,
            )
            Spacer(Modifier.height(7.dp))
            Text("Küçük oturumlar. Büyük keşifler.", color = GameColors.TextMuted)
        }
    }
}
