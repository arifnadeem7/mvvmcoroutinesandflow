package com.arif.kotlincoroutinesplusflow.features.weather

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.kotlincoroutinesplusflow.custom.aliases.WeatherResult
import com.arif.kotlincoroutinesplusflow.features.home.di.HomeScope
import com.arif.kotlincoroutinesplusflow.utils.Utils
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HomeScope
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private val mutableWeatherLiveData = MutableLiveData<WeatherResult>()

    //Exposed to View's//
    val weatherLiveData = mutableWeatherLiveData

    // Uncomment if you want to observe changes from DB
//    private val mutableWeatherData = MutableLiveData<DbWeather>()

//    val weatherData = mutableWeatherData

//    init {
//        //collect DB changes using the viewModelScope
//        viewModelScope.launch {
//            weatherRepository.getWeatherDBFlow()
//                .collect { mutableWeatherData.value = it }
//        }
//    }

    /**
     * Launch from View confining this flow to it's lifecycleScope
     * Comment this and use callWeatherApi() if you plan to observe changes from DB
     */
    @MainThread
    suspend fun getWeather() {
        weatherRepository.getWeather(Utils.LONDON_CITY)
            .collect {
                mutableWeatherLiveData.value = it
            }
    }

    /**
     * Launch from View confining this flow to it's lifecycleScope
     */
    @MainThread
    suspend fun callWeatherApi() {
        weatherRepository.callWeatherApi(Utils.LONDON_CITY)
            .collect {
                mutableWeatherLiveData.value = it
            }
    }
}
