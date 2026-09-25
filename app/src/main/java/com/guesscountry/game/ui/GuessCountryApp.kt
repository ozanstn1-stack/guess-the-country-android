package com.guesscountry.game.ui

import androidx.compose.runtime.Composable
import com.guesscountry.game.data.local.PlayerRepository
import com.guesscountry.game.data.repository.CountryRepository
import com.guesscountry.game.navigation.GuessCountryApp as AppNavigation

@Composable
fun GuessCountryApp(
    countryRepository: CountryRepository,
    playerRepository: PlayerRepository,
) {
    AppNavigation(countryRepository, playerRepository)
}
