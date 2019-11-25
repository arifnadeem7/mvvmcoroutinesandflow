package com.arif.kotlincoroutinesplusflow.features.weather

import com.arif.kotlincoroutinesplusflow.base.Error
import com.arif.kotlincoroutinesplusflow.base.Success
import com.arif.kotlincoroutinesplusflow.custom.aliases.WeatherResult
import com.arif.kotlincoroutinesplusflow.custom.errors.ErrorHandler
import com.arif.kotlincoroutinesplusflow.custom.errors.NoDataException
import com.arif.kotlincoroutinesplusflow.custom.errors.NoResponseException
import com.arif.kotlincoroutinesplusflow.entitymappers.weather.WeatherMapper
import com.arif.kotlincoroutinesplusflow.extensions.applyCommonSideEffects
import com.arif.kotlincoroutinesplusflow.features.home.di.HomeScope
import com.arif.kotlincoroutinesplusflow.network.api.OpenWeatherApi
import com.arif.kotlincoroutinesplusflow.network.response.ErrorResponse
import com.arif.kotlincoroutinesplusflow.room.dao.utils.StringKeyValueDao
import com.arif.kotlincoroutinesplusflow.room.dao.weather.WeatherDao
import com.arif.kotlincoroutinesplusflow.utils.Utils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HomeScope
class WeatherRepository @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val weatherDao: WeatherDao,
    private val stringKeyValueDao: StringKeyValueDao
) {

    private val weatherCacheThresholdMillis = 3600000L //1 hour//

    fun getWeather(cityName: String) = flow {
        stringKeyValueDao.get(Utils.LAST_WEATHER_API_CALL_TIMESTAMP)
            ?.takeIf { !Utils.shouldCallApi(it.value, weatherCacheThresholdMillis) }
            ?.let { emit(getDataOrError(NoDataException())) }
            ?: emit(getWeatherFromAPI(cityName))
        }
        .applyCommonSideEffects()
        .catch {
            emit(getDataOrError(it))
        }

    /**
     * Another way...
     *
     * Use this pattern when your data can change from different places.
     * This method calls the API and then saves it's response to the database.
     * Caller should also use the function getWeatherDBFlow() to listen for changes and
     * update the UI accordingly.
     *
     * Expected usage
     *
     * Expose an immutable LiveData from your ViewModel to observe DB changes in your View.
     *
     * In your ViewModel call this inside init{} block
     *
     *      viewModelScope.launch {
     *      weatherRepository.getWeatherDBFlow()
     *      .collect { mutableWeatherData.value = it }
     *      }
     *
     *  Then call this function callWeatherApi(cityName) from your View's lifecycleScope
     *
     */
    fun callWeatherApi(cityName: String) = flow<WeatherResult> {
        val lastTimestamp = stringKeyValueDao.get(Utils.LAST_WEATHER_API_CALL_TIMESTAMP)
        if (lastTimestamp == null || Utils.shouldCallApi(
                lastTimestamp.value,
                weatherCacheThresholdMillis
            )
        ) {
            openWeatherApi.getWeatherFromCityName(cityName)
                .run {
                    if (isSuccessful && body() != null) {
                        stringKeyValueDao.insert(
                            Utils.getCurrentTimeKeyValuePair(Utils.LAST_WEATHER_API_CALL_TIMESTAMP)
                        )
                        weatherDao.deleteAllAndInsert(WeatherMapper(body()!!).map())
                    }
                }
        }
    }.applyCommonSideEffects().catch { emit(Error(it)) }

    private suspend fun getWeatherFromAPI(cityName: String) =
        openWeatherApi.getWeatherFromCityName(cityName)
            .run {
                if (isSuccessful && body() != null) {
                    stringKeyValueDao.insert(
                        Utils.getCurrentTimeKeyValuePair(Utils.LAST_WEATHER_API_CALL_TIMESTAMP)
                    )
                    weatherDao.deleteAllAndInsert(WeatherMapper(body()!!).map())
                    getDataOrError(NoDataException())
                } else {
                    Error(
                        NoResponseException(
                            ErrorHandler.parseError<ErrorResponse>(errorBody())?.message
                        )
                    )
                }
            }

    private suspend fun getDataOrError(throwable: Throwable) =
        weatherDao.get()
            ?.let { dbValue -> Success(dbValue) }
            ?: Error(throwable)

    //Observe DB changes
    fun getWeatherDBFlow() = weatherDao.getFlow()
}