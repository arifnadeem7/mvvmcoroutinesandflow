package com.arif.kotlincoroutinesplusflow.network.response.forecast

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ApiForecast(
    val cod: String,
    val message: Double,
    val cnt: Int,
    val list: List<Forecast>,
    val city: City
)


@JsonClass(generateAdapter = true)
data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String
)


@JsonClass(generateAdapter = true)
data class Coord(
    val lat: Double,
    val lon: Double
)


@JsonClass(generateAdapter = true)
data class Forecast(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val sys: Sys,
    val dt_txt: String
)


@JsonClass(generateAdapter = true)
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)


@JsonClass(generateAdapter = true)
data class Sys(
    val pod: String
)


@JsonClass(generateAdapter = true)
data class Main(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val sea_level: Double,
    val grnd_level: Double,
    val humidity: Double,
    val temp_kf: Double
)


@JsonClass(generateAdapter = true)
data class Clouds(
    val all: Int
)


@JsonClass(generateAdapter = true)
data class Wind(
    val speed: Double,
    val deg: Double
)