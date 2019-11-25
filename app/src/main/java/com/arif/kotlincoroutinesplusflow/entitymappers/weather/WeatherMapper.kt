package com.arif.kotlincoroutinesplusflow.entitymappers.weather

import androidx.annotation.WorkerThread
import com.arif.kotlincoroutinesplusflow.entitymappers.Mapper
import com.arif.kotlincoroutinesplusflow.network.response.weather.ApiWeather
import com.arif.kotlincoroutinesplusflow.room.models.weather.*

class WeatherMapper(private val apiWeather: ApiWeather) : Mapper<DbWeather> {

    @WorkerThread
    override fun getMapping(): DbWeather {
        return DbWeather(
            WeatherData(
                Coord(apiWeather.coord.lon, apiWeather.coord.lat),
                apiWeather.base,
                Main(
                    apiWeather.main.temp,
                    apiWeather.main.pressure,
                    apiWeather.main.humidity,
                    apiWeather.main.temp_min,
                    apiWeather.main.temp_max
                ),
                apiWeather.visibility,
                Wind(apiWeather.wind.speed, apiWeather.wind.deg),
                Clouds(apiWeather.clouds.all),
                apiWeather.dt,
                Sys(
                    apiWeather.sys.type,
                    apiWeather.sys.id,
                    apiWeather.sys.message,
                    apiWeather.sys.country,
                    apiWeather.sys.sunrise,
                    apiWeather.sys.sunset
                ),
                apiWeather.id,
                apiWeather.name,
                apiWeather.cod
            ), apiWeather.weather.asSequence().map {
                (Weather(
                    it.id,
                    it.main,
                    it.description,
                    it.icon,
                    apiWeather.id
                ))
            }.toList()
        )
    }

}