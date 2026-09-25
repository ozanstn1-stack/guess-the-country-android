package com.guesscountry.game.data.local

import com.guesscountry.game.data.model.Country
import java.io.File
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CountryDataContractTest {
    @Test
    fun bundledCountryDataHasBroadCoverageAndRequiredFields() {
        val file = sequenceOf(
            File("src/main/assets/countries.json"),
            File("app/src/main/assets/countries.json"),
        ).firstOrNull(File::exists) ?: error("countries.json is missing")
        val countries = Json { ignoreUnknownKeys = true }.decodeFromString<List<Country>>(file.readText())

        assertEquals(195, countries.size)
        assertEquals(195, countries.map { it.countryCode }.toSet().size)
        assertTrue(countries.all { it.countryCode.length == 2 })
        assertTrue(countries.count { it.famousPlaces.isNotEmpty() } >= 10)
        assertTrue(countries.all { it.countryName.isNotBlank() && it.countryNameTr.isNotBlank() })
    }
}
