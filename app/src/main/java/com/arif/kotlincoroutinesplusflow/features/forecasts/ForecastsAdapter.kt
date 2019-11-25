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
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.channels.ConflatedBroadcastChannel

class ForecastsAdapter(private val channel: ConflatedBroadcastChannel<Int>) :
    ListAdapter<Forecast, ForecastsAdapter.ForecastsViewHolder>(ForecastDiff) {

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

    inner class ForecastsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val forecastItem: MaterialCardView = itemView.findViewById(R.id.card_view_forecast)
        private val tvDate: MaterialTextView = itemView.findViewById(R.id.tvDate)
        private val tvTime: MaterialTextView = itemView.findViewById(R.id.tvTime)
        private val tvMinTemp: MaterialTextView = itemView.findViewById(R.id.tv_min_temp)
        private val tvMaxTemp: MaterialTextView = itemView.findViewById(R.id.tv_max_temp)

        init {
            forecastItem.setOnClickListener(this)
        }

        fun bindData(forecast: Forecast?) {
            forecast?.apply {
                tvDate.text = Utils.getDateString(dt)
                tvTime.text = Utils.getTimeString(dt)
                tvMinTemp.text = Utils.getTemperature(main.tempMin)
                tvMaxTemp.text = Utils.getTemperature(main.tempMax)
            }
        }

        override fun onClick(v: View?) {
            if (v?.id == R.id.card_view_forecast) {
                channel.offer(adapterPosition)
            }
        }
    }
}