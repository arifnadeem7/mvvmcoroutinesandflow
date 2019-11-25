package com.arif.kotlincoroutinesplusflow.features.home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arif.kotlincoroutinesplusflow.di.ViewModelKey
import com.arif.kotlincoroutinesplusflow.di.factories.WeatherViewModelFactory
import com.arif.kotlincoroutinesplusflow.features.forecasts.ForecastsViewModel
import com.arif.kotlincoroutinesplusflow.features.weather.WeatherViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelsModule {

    @HomeScope
    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    abstract fun bindWeatherViewModel(weatherViewModel: WeatherViewModel): ViewModel

    @HomeScope
    @Binds
    @IntoMap
    @ViewModelKey(ForecastsViewModel::class)
    abstract fun bindForecastsViewModel(forecastsViewModel: ForecastsViewModel): ViewModel

    @HomeScope
    @Binds
    abstract fun bindHomeViewModelFactory(factory: WeatherViewModelFactory): ViewModelProvider.Factory
}