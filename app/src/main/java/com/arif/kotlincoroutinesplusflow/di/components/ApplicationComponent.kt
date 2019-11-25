package com.arif.kotlincoroutinesplusflow.di.components

import com.arif.kotlincoroutinesplusflow.di.modules.AppModule
import com.arif.kotlincoroutinesplusflow.di.modules.DbModule
import com.arif.kotlincoroutinesplusflow.di.modules.OpenWeatherApiModule
import com.arif.kotlincoroutinesplusflow.di.modules.SubcomponentsModule
import com.arif.kotlincoroutinesplusflow.features.home.di.HomeComponent
import com.squareup.moshi.Moshi
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, OpenWeatherApiModule::class, DbModule::class, SubcomponentsModule::class])
@Singleton
interface ApplicationComponent {
    fun getMoshi(): Moshi
    fun homeComponent(): HomeComponent.Factory
}