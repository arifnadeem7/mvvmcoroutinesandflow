package com.arif.kotlincoroutinesplusflow.room.models.weather

import androidx.room.*

data class DbWeather(
    @Embedded
    val weatherData: WeatherData,
    @Relation(parentColumn = "id", entityColumn = "weatherDataId")
    val list: List<Weather>
)

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity
data class WeatherData(
    @Embedded
    val coord: Coord,
    val base: String,
    @Embedded
    val main: Main,
    val visibility: Int,
    @Embedded
    val wind: Wind,
    @Embedded
    val clouds: Clouds,
    val dt: Long,
    @Embedded
    val sys: Sys,
    @PrimaryKey
    val id: Int,
    val name: String,
    val cod: Int
)

@Entity(primaryKeys = ["speed", "deg"])
data class Wind(
    val speed: Double,
    val deg: Double
)

@Entity(primaryKeys = ["weatherID"])
data class Weather(
    val weatherID: Int,
    val main: String,
    val description: String,
    val icon: String,
    val weatherDataId: Int
)

@Entity(primaryKeys = ["lon", "lat"])
data class Coord(
    val lon: Double,
    val lat: Double
)

@Entity(primaryKeys = ["temp", "pressure", "humidity", "tempMin", "tempMax"])
data class Main(
    val temp: Double,
    val pressure: Double,
    val humidity: Int,
    val tempMin: Double,
    val tempMax: Double
)

@Entity
data class Sys(
    val type: Int,
    @PrimaryKey
    val sysId: Int,
    val message: Double,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

@Entity
data class Clouds(
    @PrimaryKey
    val all: Int
)