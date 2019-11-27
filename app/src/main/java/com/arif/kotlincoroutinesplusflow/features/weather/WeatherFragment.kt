package com.arif.kotlincoroutinesplusflow.features.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.api.load
import com.arif.kotlincoroutinesplusflow.R
import com.arif.kotlincoroutinesplusflow.base.*
import com.arif.kotlincoroutinesplusflow.custom.aliases.WeatherResult
import com.arif.kotlincoroutinesplusflow.custom.errors.ErrorHandler
import com.arif.kotlincoroutinesplusflow.custom.views.IndefiniteSnackbar
import com.arif.kotlincoroutinesplusflow.extensions.capitalizeWords
import com.arif.kotlincoroutinesplusflow.features.home.HomeActivity
import com.arif.kotlincoroutinesplusflow.room.models.weather.DbWeather
import com.arif.kotlincoroutinesplusflow.utils.Utils
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val weatherViewModel: WeatherViewModel by viewModels { viewModelFactory }
    private lateinit var cvWeather: MaterialCardView
    private lateinit var pbHome: ContentLoadingProgressBar
    private lateinit var tvCityName: MaterialTextView
    private lateinit var tvWeatherName: MaterialTextView
    private lateinit var tvWeatherInCelsius: MaterialTextView
    private lateinit var weatherIcon: ImageView
    private lateinit var tvWeatherDescription: MaterialTextView
    private lateinit var tvMinTemp: MaterialTextView
    private lateinit var tvMaxTemp: MaterialTextView
    private lateinit var tvSunrise: MaterialTextView
    private lateinit var tvSunset: MaterialTextView
    private lateinit var btnShowForecasts: MaterialButton
    private val observer = Observer<Result<DbWeather>> { handleResponse(it) }

    //Uncomment if you would instead prefer to listen to data changes from DB
//    private val weatherDBObserver =
//        Observer<DbWeather> {
//            it?.let {
//                Timber.e("DB DATA CHANGED: ${it.weatherData.id}")
//                bindData(it)
//            }
//        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return view ?: inflater.inflate(
            R.layout.weather_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        (activity as HomeActivity).setTitle(R.string.weather)
        (activity as HomeActivity).homeComponent?.inject(this)
        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner, observer)
        //Uncomment if you would instead prefer to listen to data changes from DB
        //weatherViewModel.weatherData.observe(viewLifecycleOwner, weatherDBObserver)
        getWeather()
        super.onActivityCreated(savedInstanceState)
    }

    private fun getWeather() {
        IndefiniteSnackbar.hide()
        weatherViewModel.getWeather()
    }

    /**
     * Call this method along with weatherViewModel.weatherData.observe if you want to observe
     * changes from DB.
     */
    private fun callWeatherApi() {
        IndefiniteSnackbar.hide()
        lifecycleScope.launch {
            weatherViewModel.callWeatherApi()
        }
    }

    private fun handleResponse(result: WeatherResult) {
        when (result) {
            //comment this Success check if you are observing data from DB
            is Success<DbWeather> -> bindData(result.data)
            is Error -> {
                view?.let { view ->
                    ErrorHandler.handleError(
                        view,
                        result,
                        shouldShowSnackBar = true,
                        refreshAction = { getWeather() }
                    )
                }
            }
            is Progress -> {
                pbHome.visibility = toggleVisibility(result)
            }
        }
    }

    private fun initViews(view: View) {
        view.apply {
            cvWeather = findViewById(R.id.card_view_weather)
            pbHome = findViewById(R.id.pb_home)
            tvCityName = findViewById(R.id.tv_city_name)
            tvWeatherName = findViewById(R.id.tv_weather_name)
            tvWeatherInCelsius = findViewById(R.id.tv_weather_celsius)
            weatherIcon = findViewById(R.id.weather_icon)
            tvWeatherDescription = findViewById(R.id.tv_weather_description)
            tvMinTemp = findViewById(R.id.tv_min_temp)
            tvMaxTemp = findViewById(R.id.tv_max_temp)
            tvSunrise = findViewById(R.id.tv_sunrise)
            tvSunset = findViewById(R.id.tv_sunset)
            btnShowForecasts = findViewById(R.id.btn_show_forecasts)
        }
        btnShowForecasts.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_forecastsFragment)
        }
    }

    private fun bindData(response: DbWeather) {
        with(response) {
            tvCityName.text = weatherData.name
            tvWeatherInCelsius.text = Utils.getTemperature(weatherData.main.temp)
            list.takeIf { it.isNotEmpty() }
                ?.get(0)
                ?.let {
                    tvWeatherName.text = it.main
                    weatherIcon.load("http://openweathermap.org/img/w/${it.icon}.png")
                    tvWeatherDescription.text = it.description.capitalizeWords()
                }
            tvMinTemp.text = Utils.getTemperature(weatherData.main.tempMin)
            tvMaxTemp.text = Utils.getTemperature(weatherData.main.tempMax)
            tvSunrise.text = Utils.getTimeString(weatherData.sys.sunrise)
            tvSunset.text = Utils.getTimeString(weatherData.sys.sunset)
        }
    }

}
