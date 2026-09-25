package com.guesscountry.game.data.local

import android.content.Context
import android.util.Log
import com.guesscountry.game.data.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class JsonCountryDataSource(private val context: Context) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun load(): List<Country> = withContext(Dispatchers.IO) {
        try {
            context.assets.open(FILE_NAME).bufferedReader().use { reader ->
                json.decodeFromString<List<Country>>(reader.readText())
                    .filter { it.countryCode.length == 2 }
                    .sortedBy { it.displayName() }
            }.ifEmpty { fallbackCountries }
        } catch (exception: Exception) {
            Log.e(TAG, "Country data could not be loaded; using fallback set", exception)
            fallbackCountries
        }
    }

    private companion object {
        const val FILE_NAME = "countries.json"
        const val TAG = "JsonCountryDataSource"

        val fallbackCountries = listOf(
            Country("Turkey", "TR", "Ankara", "Europe/Asia", "Turkish lira", "TRY", "🇹🇷", famousPlaces = listOf("Ayasofya", "Kapadokya", "Pamukkale"), difficulty = "easy", countryNameTr = "Türkiye"),
            Country("Germany", "DE", "Berlin", "Europe", "Euro", "EUR", "🇩🇪", famousPlaces = listOf("Brandenburg Gate", "Neuschwanstein Castle"), difficulty = "easy", countryNameTr = "Almanya"),
            Country("France", "FR", "Paris", "Europe", "Euro", "EUR", "🇫🇷", famousPlaces = listOf("Eiffel Tower", "Louvre Museum"), difficulty = "easy", countryNameTr = "Fransa"),
            Country("Italy", "IT", "Rome", "Europe", "Euro", "EUR", "🇮🇹", famousPlaces = listOf("Colosseum", "Vatican City"), difficulty = "easy", countryNameTr = "İtalya"),
            Country("Spain", "ES", "Madrid", "Europe", "Euro", "EUR", "🇪🇸", famousPlaces = listOf("Sagrada Familia", "Alhambra"), difficulty = "easy", countryNameTr = "İspanya"),
            Country("Netherlands", "NL", "Amsterdam", "Europe", "Euro", "EUR", "🇳🇱", famousPlaces = listOf("Rijksmuseum", "Keukenhof Gardens"), difficulty = "easy", countryNameTr = "Hollanda"),
            Country("Japan", "JP", "Tokyo", "Asia", "Japanese yen", "JPY", "🇯🇵", famousPlaces = listOf("Fushimi Inari Shrine", "Mount Fuji"), difficulty = "easy", countryNameTr = "Japonya"),
            Country("Brazil", "BR", "Brasilia", "Americas", "Brazilian real", "BRL", "🇧🇷", famousPlaces = listOf("Christ the Redeemer", "Iguazu Falls"), difficulty = "easy", countryNameTr = "Brezilya"),
            Country("Canada", "CA", "Ottawa", "Americas", "Canadian dollar", "CAD", "🇨🇦", famousPlaces = listOf("Niagara Falls", "Banff National Park"), difficulty = "easy", countryNameTr = "Kanada"),
            Country("Australia", "AU", "Canberra", "Oceania", "Australian dollar", "AUD", "🇦🇺", famousPlaces = listOf("Sydney Opera House", "Uluru"), difficulty = "easy", countryNameTr = "Avustralya"),
            Country("United States", "US", "Washington, D.C.", "Americas", "United States dollar", "USD", "🇺🇸", famousPlaces = listOf("Statue of Liberty", "Grand Canyon"), difficulty = "easy", countryNameTr = "Amerika Birleşik Devletleri"),
            Country("South Korea", "KR", "Seoul", "Asia", "South Korean won", "KRW", "🇰🇷", famousPlaces = listOf("Gyeongbokgung Palace", "Bukchon Hanok Village"), difficulty = "medium", countryNameTr = "Güney Kore"),
        )
    }
}
