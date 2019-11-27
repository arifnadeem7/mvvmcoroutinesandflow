package com.arif.kotlincoroutinesplusflow.features.weather

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arif.kotlincoroutinesplusflow.custom.aliases.WeatherResult
import com.arif.kotlincoroutinesplusflow.extensions.cancelIfActive
import com.arif.kotlincoroutinesplusflow.features.home.di.HomeScope
import com.arif.kotlincoroutinesplusflow.utils.Utils
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HomeScope
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private val mutableWeatherLiveData = MutableLiveData<WeatherResult>()
    private var getWeatherJob: Job? = null

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
     * Cancel existing job if running and then launch weatherRepository.getWeather using
     * viewModelScope
     */
    fun getWeather() {
        getWeatherJob.cancelIfActive()
        getWeatherJob = viewModelScope.launch {
            weatherRepository.getWeather(Utils.LONDON_CITY)
                .collect {
                    mutableWeatherLiveData.value = it
                }
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
