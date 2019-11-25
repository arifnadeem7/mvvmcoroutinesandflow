package com.arif.kotlincoroutinesplusflow.room.models.forecasts

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class DbForecast(
    @Embedded
    val forecastData: ForecastData,
    @Relation(parentColumn = "cod", entityColumn = "forecastDataId", entity = Forecast::class)
    val list: List<ForecastAndWeather>
)

@Entity(primaryKeys = ["cod"])
data class ForecastData(
    val cod: String,
    val message: Double,
    val cnt: Int,
    @Embedded
    val city: City
)

@Entity(primaryKeys = ["cityId"])
data class City(
    val cityId: Int,
    val name: String,
    @Embedded
    val coord: Coord,
    val country: String
)

@Entity(primaryKeys = ["lat", "lon"])
data class Coord(
    val lat: Double,
    val lon: Double
)

data class ForecastAndWeather(
    @Embedded
    val forecast: Forecast,
    @Relation(parentColumn = "dt", entityColumn = "forecastId")
    val forecastWeather: List<ForecastWeather>
)

@Entity(primaryKeys = ["dt"])
data class Forecast(
    val dt: Long,
    @Embedded
    val main: Main,
    @Embedded
    val clouds: Clouds,
    @Embedded
    val wind: Wind,
    @Embedded
    val sys: Sys,
    val dtTxt: String,
    val forecastDataId: String
)

@Entity(primaryKeys = ["weatherID"])
data class ForecastWeather(
    val weatherID: Int,
    val main: String,
    val description: String,
    val icon: String,
    val forecastId: Long
)

@Entity(primaryKeys = ["pod"])
data class Sys(
    val pod: String
)

@Entity(primaryKeys = ["tempMin", "tempMax"])
data class Main(
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Double,
    val seaLevel: Double,
    val grndLevel: Double,
    val humidity: Double,
    val tempKf: Double
)

@Entity(primaryKeys = ["all"])
data class Clouds(
    val all: Int
)

@Entity(primaryKeys = ["speed", "deg"])
data class Wind(
    val speed: Double,
    val deg: Double
)