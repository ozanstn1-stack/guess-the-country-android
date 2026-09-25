package com.guesscountry.game.data.repository

import com.guesscountry.game.data.local.JsonCountryDataSource
import com.guesscountry.game.data.model.Country
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface CountryRepository {
    suspend fun getCountries(): List<Country>
}

class DefaultCountryRepository(
    private val dataSource: JsonCountryDataSource,
) : CountryRepository {
    private val mutex = Mutex()
    private var cachedCountries: List<Country>? = null

    override suspend fun getCountries(): List<Country> {
        cachedCountries?.let { return it }
        return mutex.withLock {
            cachedCountries ?: dataSource.load().also { cachedCountries = it }
        }
    }
}
