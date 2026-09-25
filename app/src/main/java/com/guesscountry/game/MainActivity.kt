package com.guesscountry.game

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.guesscountry.game.ui.GuessCountryApp
import com.guesscountry.game.ui.theme.GuessCountryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.navigationBarColor = Color.TRANSPARENT
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }
        val application = application as GuessCountryApplication
        setContent {
            GuessCountryTheme {
                GuessCountryApp(
                    countryRepository = application.countryRepository,
                    playerRepository = application.playerRepository,
                )
            }
        }
    }
}
