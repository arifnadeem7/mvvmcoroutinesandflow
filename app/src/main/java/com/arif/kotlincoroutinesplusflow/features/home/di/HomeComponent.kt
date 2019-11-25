package com.arif.kotlincoroutinesplusflow.features.home.di

import com.arif.kotlincoroutinesplusflow.features.forecasts.ForecastsFragment
import com.arif.kotlincoroutinesplusflow.features.home.HomeActivity
import com.arif.kotlincoroutinesplusflow.features.weather.WeatherFragment
import dagger.Subcomponent

@HomeScope
@Subcomponent(modules = [HomeViewModelsModule::class])
interface HomeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }

    fun inject(homeActivity: HomeActivity)
    fun inject(weatherFragment: WeatherFragment)
    fun inject(forecastsFragment: ForecastsFragment)
}