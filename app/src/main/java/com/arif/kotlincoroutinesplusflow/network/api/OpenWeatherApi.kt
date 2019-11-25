package com.arif.kotlincoroutinesplusflow.network.api

import androidx.annotation.WorkerThread
import com.arif.kotlincoroutinesplusflow.network.response.forecast.ApiForecast
import com.arif.kotlincoroutinesplusflow.network.response.weather.ApiWeather
import com.arif.kotlincoroutinesplusflow.utils.Utils
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @WorkerThread
    @GET("data/2.5/weather")
    suspend fun getWeatherFromCityName(
        @Query("q") cityName: String,
        @Query("APPID") apiKey: String = Utils.OPEN_WEATHER_MAPS_API_KEY,
        @Query("units") units: String = Utils.DEFAULT_UNIT_SYSTEM
    ): Response<ApiWeather>

    @WorkerThread
    @GET("data/2.5/forecast")
    suspend fun getWeatherForecast(
        @Query("id") id: Int,
        @Query("APPID") apiKey: String = Utils.OPEN_WEATHER_MAPS_API_KEY,
        @Query("units") units: String = Utils.DEFAULT_UNIT_SYSTEM
    ): Response<ApiForecast>

}