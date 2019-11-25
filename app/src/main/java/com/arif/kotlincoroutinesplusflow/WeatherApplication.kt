package com.arif.kotlincoroutinesplusflow

import android.app.Application
import com.arif.kotlincoroutinesplusflow.di.components.ApplicationComponent
import com.arif.kotlincoroutinesplusflow.di.components.DaggerApplicationComponent
import com.arif.kotlincoroutinesplusflow.di.modules.AppModule
import com.arif.kotlincoroutinesplusflow.di.modules.DbModule
import com.arif.kotlincoroutinesplusflow.di.modules.OpenWeatherApiModule
import com.squareup.moshi.Moshi
import timber.log.Timber

class WeatherApplication : Application() {

    lateinit var appComponent: ApplicationComponent

    companion object {
        lateinit var moshi: Moshi
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initDaggerComponent()
        moshi = appComponent.getMoshi()
    }

    private fun initDaggerComponent() {
        appComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(applicationContext))
            .dbModule(DbModule())
            .openWeatherApiModule(OpenWeatherApiModule())
            .build()
    }
}