package com.arif.kotlincoroutinesplusflow.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arif.kotlincoroutinesplusflow.room.dao.forecasts.ForecastDao
import com.arif.kotlincoroutinesplusflow.room.dao.utils.StringKeyValueDao
import com.arif.kotlincoroutinesplusflow.room.dao.weather.WeatherDao
import com.arif.kotlincoroutinesplusflow.room.models.forecasts.Forecast
import com.arif.kotlincoroutinesplusflow.room.models.forecasts.ForecastData
import com.arif.kotlincoroutinesplusflow.room.models.forecasts.ForecastWeather
import com.arif.kotlincoroutinesplusflow.room.models.utils.StringKeyValuePair
import com.arif.kotlincoroutinesplusflow.room.models.weather.Weather
import com.arif.kotlincoroutinesplusflow.room.models.weather.WeatherData

@Database(
    entities = [WeatherData::class, Weather::class, ForecastData::class, Forecast::class,
        ForecastWeather::class, StringKeyValuePair::class], version = 1
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun keyValueDao(): StringKeyValueDao
    abstract fun forecastDao(): ForecastDao
}