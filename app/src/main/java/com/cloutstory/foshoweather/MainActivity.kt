package com.cloutstory.foshoweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*
import com.cloutstory.foshoweather.httpRequests.CustomListener
import com.cloutstory.foshoweather.httpRequests.ApiUtils
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    val context = this
    val city = "Maple Grove"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWeatherIcon()
        ApiUtils.getInstance(this)
        populateCurrentWeatherData()
    }

    fun stringToWeatherData(WeatherJsonString: String): JsonObject {
        val data = JsonParser.parseString(WeatherJsonString).asJsonObject
        return JsonParser.parseString(WeatherJsonString).asJsonObject
    }

    fun populateCurrentWeatherData() {
        val fetchWeatherListener = object : CustomListener<String> {
            override fun getResult(result: String) {
              if(result.isNotEmpty()) {
                  Log.d("Response", result)
                  val weatherData = stringToWeatherData(result)
                  //city
                  val cityText = findViewById<TextView>(R.id.city)
                  cityText.text = weatherData.get("name").toString().substringAfter('"').substringBefore('"').toUpperCase()
                  //temp
                  val tempInt = weatherData.get("main").asJsonObject.get("temp").toString().toDouble()
                  val temperatureText = findViewById<TextView>(R.id.temperature)
                  val tempF = tempInt * 1.8 + 32
                  temperatureText.text = tempF.toInt().toString()
                  Log.d("Temperature", "Temp ATM: $tempF")
                  //Feels Like
                  val feelInt = weatherData.get("main").asJsonObject.get("feels_like").toString().toDouble()
                  val feelsText = findViewById<TextView>(R.id.feelsLike)
                  val feelsLikeF = (feelInt * 1.8 + 32).toInt()
                  feelsText.text = "FEELS LIKE $feelsLikeF°"
              } else {
                  Log.e("ERROR", "Weather Data is empty")
              }
            }
        }
        ApiUtils.getInstance()
            ?.fetchWeather(city, listener = fetchWeatherListener)
    }
    //Example: How to change Weather Icon
    fun getWeatherIcon() {
        val weatherIconUrl = findViewById<ImageView>(R.id.weatherIcon)
        val weatherSunny = R.drawable.icon_sunny
        var weather: String = "sunny"
        if (weather == "sunny") {
            Picasso.get().load(weatherSunny).into(weatherIconUrl);
        }
    }
}