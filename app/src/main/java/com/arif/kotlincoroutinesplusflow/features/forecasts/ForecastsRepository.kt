package com.arif.kotlincoroutinesplusflow.features.forecasts

import com.arif.kotlincoroutinesplusflow.base.Error
import com.arif.kotlincoroutinesplusflow.base.Success
import com.arif.kotlincoroutinesplusflow.custom.errors.ErrorHandler
import com.arif.kotlincoroutinesplusflow.custom.errors.NoDataException
import com.arif.kotlincoroutinesplusflow.custom.errors.NoResponseException
import com.arif.kotlincoroutinesplusflow.entitymappers.forecasts.ForecastMapper
import com.arif.kotlincoroutinesplusflow.extensions.applyCommonSideEffects
import com.arif.kotlincoroutinesplusflow.features.home.di.HomeScope
import com.arif.kotlincoroutinesplusflow.network.api.OpenWeatherApi
import com.arif.kotlincoroutinesplusflow.network.response.ErrorResponse
import com.arif.kotlincoroutinesplusflow.room.dao.forecasts.ForecastDao
import com.arif.kotlincoroutinesplusflow.room.dao.utils.StringKeyValueDao
import com.arif.kotlincoroutinesplusflow.room.models.forecasts.DbForecast
import com.arif.kotlincoroutinesplusflow.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HomeScope
class ForecastsRepository @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val forecastDao: ForecastDao,
    private val stringKeyValueDao: StringKeyValueDao
) {

    private val forecastCacheThresholdMillis = 3 * 3600000L //3 hours//

    fun getForecasts(cityId: Int) = flow {
        stringKeyValueDao.get(Utils.LAST_FORECASTS_API_CALL_TIMESTAMP)
            ?.takeIf { !Utils.shouldCallApi(it.value, forecastCacheThresholdMillis) }
            ?.let { emit(getDataOrError(NoDataException())) }
            ?: emit((getForecastFromAPI(cityId)))
        }
        .applyCommonSideEffects()
        .catch {
            emit(getDataOrError(it))
        }

    private suspend fun getForecastFromAPI(cityId: Int) = openWeatherApi.getWeatherForecast(cityId)
        .run {
            if (isSuccessful && body() != null) {
                stringKeyValueDao.insert(
                    Utils.getCurrentTimeKeyValuePair(Utils.LAST_FORECASTS_API_CALL_TIMESTAMP)
                )
                forecastDao.deleteAllAndInsert(ForecastMapper(body()!!).map())
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
        forecastDao.get()
            ?.let { dbValue -> Success(getForecastList(dbValue)) }
            ?: Error(throwable)

    private suspend fun getForecastList(dbForecast: DbForecast) = withContext(Dispatchers.Default) {
        dbForecast.list.map { it.forecast }
    }
}
