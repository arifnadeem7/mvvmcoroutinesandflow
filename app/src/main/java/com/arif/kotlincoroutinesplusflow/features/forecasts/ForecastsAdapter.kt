package com.arif.kotlincoroutinesplusflow.features.forecasts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arif.kotlincoroutinesplusflow.R
import com.arif.kotlincoroutinesplusflow.room.models.forecasts.Forecast
import com.arif.kotlincoroutinesplusflow.utils.Utils
import com.google.android.material.textview.MaterialTextView

class ForecastsAdapter : ListAdapter<Forecast, ForecastsAdapter.ForecastsViewHolder>(ForecastDiff) {

    private object ForecastDiff : DiffUtil.ItemCallback<Forecast>() {

        override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast) =
            oldItem.dt == newItem.dt

        override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastsViewHolder {
        return ForecastsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.forecast_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ForecastsViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    class ForecastsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: MaterialTextView = itemView.findViewById(R.id.tvDate)
        private val tvTime: MaterialTextView = itemView.findViewById(R.id.tvTime)
        private val tvMinTemp: MaterialTextView = itemView.findViewById(R.id.tv_min_temp)
        private val tvMaxTemp: MaterialTextView = itemView.findViewById(R.id.tv_max_temp)

        fun bindData(forecast: Forecast?) {
            forecast?.apply {
                tvDate.text = Utils.getDateString(dt)
                tvTime.text = Utils.getTimeString(dt)
                tvMinTemp.text = Utils.getTemperature(main.tempMin)
                tvMaxTemp.text = Utils.getTemperature(main.tempMax)
            }
        }
    }
}