package com.cloutstory.foshoweather

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWeatherIcon()
    }
    fun getWeatherIcon () {
        val weatherIconUrl = findViewById<ImageView>(R.id.weatherIcon)
        val weatherSunny = R.drawable.icon_sunny
        var weather: String
        weather = "sunny"
        if (weather == "sunny") {
            Picasso.get().load(weatherSunny).into(weatherIconUrl);
        }
    }
}