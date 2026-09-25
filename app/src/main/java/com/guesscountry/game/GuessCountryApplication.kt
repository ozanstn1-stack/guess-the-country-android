package com.guesscountry.game

import android.app.Application
import com.guesscountry.game.data.local.JsonCountryDataSource
import com.guesscountry.game.data.local.PlayerRepository
import com.guesscountry.game.data.repository.DefaultCountryRepository

class GuessCountryApplication : Application() {
    val countryRepository by lazy {
        DefaultCountryRepository(JsonCountryDataSource(this))
    }

    val playerRepository by lazy {
        PlayerRepository(this)
    }
}
