package com.arif.kotlincoroutinesplusflow.features.forecasts

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arif.kotlincoroutinesplusflow.custom.aliases.ForecastResults
import com.arif.kotlincoroutinesplusflow.features.home.di.HomeScope
import com.arif.kotlincoroutinesplusflow.utils.Utils
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HomeScope
class ForecastsViewModel @Inject constructor(private val forecastsRepository: ForecastsRepository) :
    ViewModel() {

    private val mutableForecastLiveData = MutableLiveData<ForecastResults>()

    val forecastLiveData = mutableForecastLiveData

    /**
     * Launch from View confining this flow to it's lifecycleScope
     */
    @MainThread
    suspend fun getForecasts() {
        forecastsRepository.getForecasts(Utils.LONDON_CITY_ID).collect {
            mutableForecastLiveData.value = it
        }
    }
}