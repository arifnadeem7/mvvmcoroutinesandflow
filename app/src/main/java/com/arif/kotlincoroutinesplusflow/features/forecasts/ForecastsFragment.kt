package com.arif.kotlincoroutinesplusflow.features.forecasts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arif.kotlincoroutinesplusflow.R
import com.arif.kotlincoroutinesplusflow.base.*
import com.arif.kotlincoroutinesplusflow.custom.aliases.ForecastResults
import com.arif.kotlincoroutinesplusflow.custom.aliases.ListOfForecasts
import com.arif.kotlincoroutinesplusflow.custom.errors.ErrorHandler
import com.arif.kotlincoroutinesplusflow.custom.views.IndefiniteSnackbar
import com.arif.kotlincoroutinesplusflow.custom.views.SpacesItemDecoration
import com.arif.kotlincoroutinesplusflow.features.home.HomeActivity
import com.arif.kotlincoroutinesplusflow.utils.Utils
import javax.inject.Inject

class ForecastsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val forecastsViewModel: ForecastsViewModel by viewModels { viewModelFactory }
    private lateinit var forecastsAdapter: ForecastsAdapter
    private lateinit var forecastsRecycler: RecyclerView
    private lateinit var pbForecasts: ContentLoadingProgressBar
    private val observer = Observer<ForecastResults> { handleResponse(it) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return view ?: inflater.inflate(R.layout.forecasts_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        (activity as HomeActivity).title = getString(R.string.forecast, Utils.LONDON_CITY)
        (activity as HomeActivity).homeComponent?.inject(this)
        forecastsViewModel.forecastLiveData.observe(viewLifecycleOwner, observer)
        getForecasts()
        super.onActivityCreated(savedInstanceState)
    }

    private fun getForecasts() {
        IndefiniteSnackbar.hide()
        forecastsViewModel.getForecasts()
    }

    private fun initViews(view: View) {
        view.apply {
            forecastsRecycler = findViewById(R.id.forecasts_recycler)
            pbForecasts = findViewById(R.id.pb_forecasts)
        }
        forecastsRecycler.layoutManager = LinearLayoutManager(context)
        val startMargin = resources.getDimension(R.dimen.margin).toInt()
        val topMargin = resources.getDimension(R.dimen.margin_small).toInt()
        forecastsRecycler.addItemDecoration(SpacesItemDecoration(topMargin, startMargin))
        forecastsAdapter = ForecastsAdapter()
        forecastsRecycler.adapter = forecastsAdapter
    }

    private fun handleResponse(it: Result<ListOfForecasts>) {
        when (it) {
            is Success<ListOfForecasts> -> bindData(it.data)
            is Error -> view?.let { view ->
                ErrorHandler.handleError(
                    view,
                    it,
                    shouldShowSnackBar = true,
                    refreshAction = { getForecasts() })
            }
            is Progress -> {
                pbForecasts.visibility = toggleVisibility(it)
            }
        }
    }

    private fun bindData(forecasts: ListOfForecasts) {
        forecastsAdapter.submitList(forecasts)
    }
}