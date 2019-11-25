package com.arif.kotlincoroutinesplusflow.custom.aliases

import com.arif.kotlincoroutinesplusflow.base.Result
import com.arif.kotlincoroutinesplusflow.room.models.forecasts.Forecast
import com.arif.kotlincoroutinesplusflow.room.models.weather.DbWeather

typealias WeatherResult = Result<DbWeather>

typealias ListOfForecasts = List<Forecast>

typealias ForecastResults = Result<ListOfForecasts>