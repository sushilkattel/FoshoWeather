package com.cloutstory.foshoweather.httpRequests
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cloutstory.foshoweather.BuildConfig

class ApiUtils private constructor(context: Context) {
    private var requestQueue: RequestQueue = Volley.newRequestQueue(context.applicationContext)

    fun fetchWeather (lat: String, lon: String, listener: CustomListener<String> ) {
        //api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
        //val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&APPID=$APIKey&units=metric&lang=en"
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$APIKey&units=imperial"
        //val url = "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&exclude=hourly&appid=$APIKey&units=imperial"

        val request = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                if (response != null) listener.getResult(response)
            },
            Response.ErrorListener { error ->
                Log.e(TAG, error.toString())
                listener.getResult("")
            }
        )
        requestQueue.add(request)
    }

    fun fetchWeatherForecast (lat: String, lon: String, listener: CustomListener<String> ) {
        //api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
        //val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&APPID=$APIKey&units=metric&lang=en"
        //val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$APIKey&units=imperial"
        val url = "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&exclude=minutely&appid=$APIKey&units=imperial"

        val request = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    if (response != null) listener.getResult(response)
                },
                Response.ErrorListener { error ->
                    Log.e(TAG, error.toString())
                    listener.getResult("")
                }
        )
        requestQueue.add(request)
    }

    companion object {
        private const val TAG = "ApiUtils"
        private var instance: ApiUtils? = null
        const val APIKey = BuildConfig.WEATHER_API_KEY

        @Synchronized
        fun getInstance(context: Context): ApiUtils? {
            if (null == instance) instance =
                ApiUtils(context)
            return instance
        }

        //this is so you don't need to pass context each time
        @Synchronized
        fun getInstance(): ApiUtils? {
            checkNotNull(instance) {
                ApiUtils::class.java.simpleName +
                        " is not initialized, call getInstance(...) first"
            }
            return instance
        }
    }
}