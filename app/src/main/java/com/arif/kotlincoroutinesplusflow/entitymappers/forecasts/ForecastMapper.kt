package com.arif.kotlincoroutinesplusflow.entitymappers.forecasts

import androidx.annotation.WorkerThread
import com.arif.kotlincoroutinesplusflow.entitymappers.Mapper
import com.arif.kotlincoroutinesplusflow.network.response.forecast.ApiForecast
import com.arif.kotlincoroutinesplusflow.room.models.forecasts.*

class ForecastMapper(private val apiForecast: ApiForecast) : Mapper<DbForecast> {

    @WorkerThread
    override fun getMapping(): DbForecast {
        return DbForecast(
            ForecastData(
                apiForecast.cod,
                apiForecast.message,
                apiForecast.cnt,
                City(
                    apiForecast.city.id,
                    apiForecast.city.name,
                    Coord(apiForecast.city.coord.lat, apiForecast.city.coord.lon),
                    apiForecast.city.country
                )
            ),
            apiForecast.list.asSequence().map {
                ForecastAndWeather(
                    Forecast(
                        it.dt,
                        Main(
                            it.main.temp,
                            it.main.temp_min,
                            it.main.temp_max,
                            it.main.pressure,
                            it.main.sea_level,
                            it.main.grnd_level,
                            it.main.humidity,
                            it.main.temp_kf
                        ),
                        Clouds(it.clouds.all),
                        Wind(it.wind.speed, it.wind.deg),
                        Sys(it.sys.pod),
                        it.dt_txt,
                        apiForecast.cod
                    ), it.weather.asSequence().map { weather ->
                        ForecastWeather(
                            weather.id,
                            weather.main,
                            weather.description,
                            weather.icon,
                            it.dt
                        )
                    }.toList()
                )
            }.toList()
        )
    }
}