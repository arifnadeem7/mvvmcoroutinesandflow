package com.arif.kotlincoroutinesplusflow.utils

import com.arif.kotlincoroutinesplusflow.room.models.utils.StringKeyValuePair
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    const val OPEN_WEATHER_MAPS_API_KEY = ""
    const val DEFAULT_UNIT_SYSTEM = "metric"
    const val LONDON_CITY = "London"
    const val BASE_URL = "http://api.openweathermap.org/"
    const val LONDON_CITY_ID = 2643743
    const val DATABASE_NAME = "forecastWeather-app"
    const val LAST_WEATHER_API_CALL_TIMESTAMP = "last_weather_api_call_timestamp"
    const val LAST_FORECASTS_API_CALL_TIMESTAMP = "last_forecasts_api_call_timestamp"
    const val MAX_RETRIES = 3L
    private const val INITIAL_BACKOFF = 2000L

    private var formatter = SimpleDateFormat("h:mm aa", Locale.getDefault())
    private var dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val df = DecimalFormat("###.#")

    fun getTimeString(timeInMillis: Long): String {
        return formatter.format(Date(timeInMillis * 1000))
    }

    fun getDateString(timeInMillis: Long): String {
        return dateFormatter.format(Date(timeInMillis * 1000))
    }

    fun getTemperature(temp: Double): String {
        return "${df.format(temp)}°C"
    }

    fun shouldCallApi(
        lastApiCallMillis: String,
        cacheThresholdInMillis: Long = 300000L //default value is 5 minutes//
    ): Boolean {
        return (System.currentTimeMillis() - lastApiCallMillis.toLong()) >= cacheThresholdInMillis
    }

    fun getCurrentTimeKeyValuePair(key: String): StringKeyValuePair {
        return StringKeyValuePair(key, System.currentTimeMillis().toString())
    }

    fun getBackoffDelay(attempt: Long) = INITIAL_BACKOFF * (attempt + 1)
}