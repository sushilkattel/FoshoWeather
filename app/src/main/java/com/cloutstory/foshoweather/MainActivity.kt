package com.cloutstory.foshoweather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.cloutstory.foshoweather.httpRequests.CustomListener
import com.cloutstory.foshoweather.httpRequests.ApiUtils
import com.cloutstory.foshoweather.models.DailyDataModels.DailyMetaDataTemp
import com.cloutstory.foshoweather.models.DailyMetaData
import com.cloutstory.foshoweather.models.HourlyMetaData
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

import com.google.gson.Gson
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import org.json.JSONArray
import org.json.JSONObject
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.time.*
import java.time.Instant.now
import java.time.Instant.ofEpochSecond
import java.util.*

class MainActivity : AppCompatActivity() {
    val context = this
    var city = "Maple Grove"
    val timeHours = Calendar.HOUR_OF_DAY
    lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps : Location? = null
    private var locationNetwork: Location? = null
    var longitudeUser: String = "-87.623177"
    var latitudeUser: String = "41.881832"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Functions
        ApiUtils.getInstance(this)
        getLocationBtn()
        getLocation()
        createHourlyCardView()
        getWeatherIcon()
        populateDailyWeatherData()
        populateCurrentWeatherData()
        getCity()

    }
    //get City
    fun getCity () {
        val googleApiKey = "AIzaSyAa1Nk32qmyINwXwxPqc5bX9yvfplc7v40"
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, googleApiKey)
        }
        val placesClient: PlacesClient = Places.createClient(this)
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES)


        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.d("PLACES", "Place: ${place.name}, ${place.latLng?.latitude}")
                latitudeUser = place.latLng?.latitude.toString()
                longitudeUser = place.latLng?.longitude.toString()
                populateCurrentWeatherData()
                createHourlyCardView()
                getWeatherIcon()
                populateDailyWeatherData()
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.d("PLACES", "An error occurred: $status")
            }
        })
    }
    //get Location

    @SuppressLint("MissingPermission")
    fun getLocation () {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
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
                        createHourlyCardView()
                        getWeatherIcon()
                        populateDailyWeatherData()
                    }else {
                        Log.d("AndroidLocationStatus, ","GPS Latitude: "+ locationGps!!.latitude)
                        Log.d("AndroidLocationStatus, ","GPS Longitude: "+ locationGps!!.longitude)
                        longitudeUser = locationNetwork!!.longitude.toString()
                        latitudeUser = locationNetwork!!.latitude.toString()
                        populateCurrentWeatherData()
                        createHourlyCardView()
                        getWeatherIcon()
                        populateDailyWeatherData()
                    }
                }

            }else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
    }

    fun getLocationBtn() {
        val buttonRefresh: Button = findViewById<Button>(R.id.getLocationBtn)
        buttonRefresh.setOnClickListener{
            getLocation()
            populateCurrentWeatherData()
            createHourlyCardView()
            getWeatherIcon()
            populateDailyWeatherData()
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
    //Weather Icon and Description
    private fun getWeatherIcon () {
        val fetchWeatherForecastListener = object : CustomListener<String> {
            override fun getResult(result: String) {
                if(result.isNotEmpty()) {
                    val descriptionResponse = stringToWeatherForecast(result).get("current").asJsonObject.get("weather").asJsonArray[0].asJsonObject.get("main").toString().substringAfter('"').substringBefore('"')
                    val hourlyIcon = stringToWeatherForecast(result).get("current").asJsonObject.get("weather").asJsonArray[0].asJsonObject.get("icon").toString().substringAfter('"').substringBefore('"')
                    //URL: http://openweathermap.org/img/wn/10d@2x.png
                    //View Finders
                    val weatherIconView = findViewById<ImageView>(R.id.weatherIcon)
                    val descriptionTextView = findViewById<TextView>(R.id.description)
                    descriptionTextView.text = "$descriptionResponse AF"
                    if (hourlyIcon !=null) {
                        Picasso.get().load("https://foshoweather.s3.amazonaws.com/icons/$hourlyIcon.png").into(weatherIconView)
                    }
                } else {
                    Log.d("ICON", "Could not retrieve data")

                }
            }

        }
        ApiUtils.getInstance()
                ?.fetchWeatherForecast(lat = latitudeUser, lon = longitudeUser, listener = fetchWeatherForecastListener)
    }

    //Populate Weather Data
    private fun populateCurrentWeatherData() {
        val fetchWeatherListener = object : CustomListener<String> {
            @RequiresApi(Build.VERSION_CODES.O)
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
                  //LocalTime
                  val now = currentTimeMillis()/1000
                  fun getCurrentTime(): Int {
                      val calendar = Calendar.getInstance()
                      return calendar.get(Calendar.HOUR)
                  }
                  val time = getCurrentTime()
                  Log.d("TIME", "Response2: $time")
                  Log.d("TIME", "Response: $now")
                  /*//weatherIcon
                  val weatherIconView = findViewById<ImageView>(R.id.weatherIcon)
                  val weatherSunny = R.drawable.icon_sunny
                  val weather = weatherData.get("weather").asJsonArray[0].asJsonObject.get("icon").toString().replace("\"", "")
                  val weatherIconUrl = "http://openweathermap.org/img/w/$weather.png"
                  Log.d("ICON", "Result: $weatherIconUrl")
                  //Picasso.get().load("https://drive.google.com/uc?id=1MNo_7gyDzrJ3SCIdzQSQyTsnl7tSkc6F").into(weatherIconView)
                  //Picasso.get().load("https://openweathermap.org/img/w/$weather.png").into(weatherIconView) */
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
                    val hourlyListJson = forecastData.get("hourly").asJsonArray.drop(1).toString()
                    val dt = forecastData.get("current").asJsonObject.get("dt").toString()
                    Log.d("UNIX", "Response: $dt")
                    Log.d("HOURS","Response: $timeHours")

                    fun hourlyJsonToArray(result: String): Array<HourlyMetaData>? {
                        val gson = Gson()
                        return gson.fromJson(hourlyListJson, Array<HourlyMetaData>::class.java)
                    }
                    val hourlyArray = hourlyJsonToArray(result)
                    val hourlyArrayResponse = hourlyArray?.get(3).toString()
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
    //Populate Daily Weather Data
    private fun populateDailyWeatherData() {
        val fetchWeatherForecastListener = object : CustomListener<String> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun getResult(result: String) {
                if(result.isNotEmpty()) {
                    val dailyWeatherJson = stringToWeatherForecast(result).get("daily").asJsonArray.toString()
                    //Json excluding first array object
                    val dailyWeatherJsonParse = stringToWeatherForecast(result).get("daily").asJsonArray.drop(1).toString()
                    Log.d("DAILY", "Response: $dailyWeatherJson")
                    fun dailyJsonToArray(result: String): Array<DailyMetaData>? {
                        val gson = Gson()
                        return gson.fromJson(dailyWeatherJsonParse, Array<DailyMetaData>::class.java)
                    }
                    val dailyArray = dailyJsonToArray(result)
                    //Update High Low Weather
                    val highWeatherText = findViewById<TextView>(R.id.highTemperature)
                    val lowWeatherText = findViewById<TextView>(R.id.lowTemperature)
                    val highWeather = stringToWeatherForecast(result).get("daily").asJsonArray.get(0).asJsonObject.get("temp").asJsonObject.get("max").toString().toFloat().toInt().toString()
                    val lowWeather = stringToWeatherForecast(result).get("daily").asJsonArray.get(0).asJsonObject.get("temp").asJsonObject.get("min").toString().toFloat().toInt().toString()
                    highWeatherText.text = "H:" + highWeather + "°"
                    lowWeatherText.text = "L:" + lowWeather + "°"

                    if (dailyArray != null) {
                        val dailyCardRecyclerView = findViewById<RecyclerView>(R.id.dailyRecyclerView)
                        dailyCardRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        val dailyCardAdapter = DailyCardAdapter(dailyArray)
                        dailyCardRecyclerView.adapter = dailyCardAdapter
                    }
                    /* Get High Temp Example
                    val dailyTempArray = JSONObject((dailyArray?.get(0)?.temp).toString())
                    val dailyTemp = dailyTempArray.get("day")
                    Log.d("DAILY", "Result: $dailyTemp") */
                    /* Get Time Example
                    val netDailyTime = dailyWeather[0].asJsonObject.get("dt").toString().toLong()
                    val dailyTime = LocalDateTime.ofInstant(ofEpochSecond(netDailyTime), ZoneId.systemDefault()).dayOfWeek.toString()
                    Log.d("DAILY", "Response: $dailyTime") */
                }
            }

        }
        ApiUtils.getInstance()
                ?.fetchWeatherForecast(lat = latitudeUser, lon = longitudeUser, listener = fetchWeatherForecastListener)
    }
    //Example: How to change Weather Icon
    /*fun getWeatherIcon() {
        val weatherIconUrl = findViewById<ImageView>(R.id.weatherIcon)
        val weatherSunny = R.drawable.icon_sunny
        var weather: String = "sunny"
        if (weather == "sunny") {
            Picasso.get().load(weatherSunny).into(weatherIconUrl);
        }
    } */
}