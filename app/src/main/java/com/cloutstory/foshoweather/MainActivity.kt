package com.cloutstory.foshoweather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.cloutstory.foshoweather.httpRequests.CustomListener
import com.cloutstory.foshoweather.httpRequests.ApiUtils
import com.cloutstory.foshoweather.models.HourlyMetaData

import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    val context = this
    var city = "Maple Grove"
    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps : Location? = null
    private var locationNetwork: Location? = null
    var longitudeUser: String = ""
    var latitudeUser: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWeatherIcon()
        ApiUtils.getInstance(this)
        getLocationBtn()
        getLocation()
        createHourlyCardView()
    }
    //get Location

    @SuppressLint("MissingPermission")
    fun getLocation () {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(hasGps || hasNetwork) {

            if(hasGps) {
                Log.d("AndroidLocationStatus", "hasGPS")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object: LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(location !=null) {
                            locationGps = location
                        }
                    }
                })
                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation !=null) {
                    locationGps = localGpsLocation
                }
            }
            if(hasNetwork) {
                Log.d("AndroidLocationStatus", "hasNetwork")
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object: LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(location !=null) {
                            locationNetwork = location
                        }
                    }
                })
                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localNetworkLocation !=null) {
                    locationNetwork = localNetworkLocation
                }
            }

            if(locationGps!= null && locationNetwork !=null) {
                if (locationGps!!.accuracy > locationNetwork!!.accuracy) {
                    Log.d("AndroidLocationStatus, ","Network Latitude: "+ locationNetwork!!.latitude)
                    Log.d("AndroidLocationStatus, ","Network Longitude: "+ locationNetwork!!.longitude)
                    longitudeUser = locationNetwork!!.longitude.toString()
                    latitudeUser = locationNetwork!!.latitude.toString()
                    populateCurrentWeatherData()
                }else {
                    Log.d("AndroidLocationStatus, ","GPS Latitude: "+ locationGps!!.latitude)
                    Log.d("AndroidLocationStatus, ","GPS Longitude: "+ locationGps!!.longitude)
                    longitudeUser = locationNetwork!!.longitude.toString()
                    latitudeUser = locationNetwork!!.latitude.toString()
                    populateCurrentWeatherData()
                }
            }

        }else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
    fun getLocationBtn() {
        val buttonRefresh: Button = findViewById<Button>(R.id.getLocationBtn)
        buttonRefresh.setOnClickListener{
            getLocation()
            populateCurrentWeatherData()
        }
    }

    //String to Json Object
    fun stringToWeatherData(WeatherJsonString: String): JsonObject {
        val data = JsonParser.parseString(WeatherJsonString).asJsonObject
        return JsonParser.parseString(WeatherJsonString).asJsonObject
    }
    //String to Json Object
    fun stringToWeatherForecast(WeatherForecastString: String): JsonObject {
        val data = JsonParser.parseString(WeatherForecastString).asJsonObject
        return data
    }

    //Populate Weather Data
    private fun populateCurrentWeatherData() {
        val fetchWeatherListener = object : CustomListener<String> {
            override fun getResult(result: String) {
              if(result.isNotEmpty()) {
                  Log.d("Response", result)
                  val weatherData = stringToWeatherData(result)
                  //city
                  val cityText = findViewById<TextView>(R.id.city)
                  cityText.text = weatherData.get("name").toString().substringAfter('"').substringBefore('"').toUpperCase()
                  //temp
                  val temp = weatherData.get("main").asJsonObject.get("temp").toString().toDouble().toInt().toString()
                  val temperatureText = findViewById<TextView>(R.id.temperature)
                  temperatureText.text = temp.toInt().toString()
                  Log.d("Temperature", "Temp ATM: $temp")
                  //Feels Like
                  val feel = weatherData.get("main").asJsonObject.get("feels_like").toString().toDouble().toInt().toString()
                  val feelsText = findViewById<TextView>(R.id.feelsLike)
                  feelsText.text = "FEELS LIKE $feel°"

                  //Location *EXPERIMENTAL*
              } else {
                  Log.e("ERROR", "Weather Data is empty")
              }
            }
        }
        ApiUtils.getInstance()
            ?.fetchWeather(lat = latitudeUser, lon = longitudeUser, listener = fetchWeatherListener)
    }
    //Populate Hourly Weather Data
    private fun createHourlyCardView() {
        val fetchWeatherForecastListener = object : CustomListener<String> {
            override fun getResult(result: String) {
                if (result.isNotEmpty()) {
                    val forecastData = stringToWeatherForecast(result)
                    val hourlyListJson = forecastData.get("hourly").asJsonArray

                    fun hourlyJsonToArray(result: String): Array<HourlyMetaData>? {
                        val gson = Gson()
                        return gson.fromJson(hourlyListJson, Array<HourlyMetaData>::class.java)
                    }
                    val hourlyArray = hourlyJsonToArray(result)
                    val hourlyArrayResponse = hourlyArray?.get(1).toString()
                    Log.d("HourlyResult", hourlyArrayResponse)
                    if (hourlyArray != null) {
                        val hourlyCardRecyclerView = findViewById<RecyclerView>(R.id.hourlyTempRecyclerView)
                        hourlyCardRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                        val hourlyCardAdapter = HourlyCardAdapter(hourlyArray)
                        hourlyCardRecyclerView.adapter = hourlyCardAdapter
                    }
                } else {
                    Log.d("HourlyResult", "Could not Retrieve HourlyResult")
                }
            }

        }
        ApiUtils.getInstance()
                ?.fetchWeatherForecast(lat = latitudeUser, lon = longitudeUser, listener = fetchWeatherForecastListener)
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