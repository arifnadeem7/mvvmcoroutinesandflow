package com.arif.kotlincoroutinesplusflow.di.modules

import android.content.Context
import androidx.room.Room
import com.arif.kotlincoroutinesplusflow.room.db.WeatherDatabase
import com.arif.kotlincoroutinesplusflow.utils.Utils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DbModule {

    @Provides
    @Singleton
    fun provideWeatherDB(context: Context): WeatherDatabase {
        return Room.databaseBuilder(context, WeatherDatabase::class.java, Utils.DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(weatherDatabase: WeatherDatabase) =
        weatherDatabase.weatherDao()

    @Provides
    @Singleton
    fun provideForecastDao(weatherDatabase: WeatherDatabase) =
        weatherDatabase.forecastDao()

    @Provides
    @Singleton
    fun provideStringKeyValueDao(weatherDatabase: WeatherDatabase) =
        weatherDatabase.keyValueDao()

}