package com.arif.kotlincoroutinesplusflow.features.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arif.kotlincoroutinesplusflow.R
import com.arif.kotlincoroutinesplusflow.WeatherApplication
import com.arif.kotlincoroutinesplusflow.features.home.di.HomeComponent


class HomeActivity : AppCompatActivity() {

    var homeComponent: HomeComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        homeComponent = (applicationContext as WeatherApplication)
            .appComponent.homeComponent().create()
        homeComponent?.inject(this)
    }
}
