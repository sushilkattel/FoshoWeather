package com.cloutstory.foshoweather.models

import com.cloutstory.foshoweather.models.DailyDataModels.DailyMetaDataFeelsLike
import com.cloutstory.foshoweather.models.DailyDataModels.DailyMetaDataTemp
import com.cloutstory.foshoweather.models.DailyDataModels.DailyMetaDataWeather

data class DailyMetaData (
        var dt: Long? = null,
        var sunrise: Long? = null,
        var sunset: Long? = null,
        var temp: Object? = null,
        var feels_like: Object? = null,
        var pressure: Int? = null,
        var humidity: Int? = null,
        var dew_point: Float? = null,
        var wind_speed: Float? = null,
        var wind_deg: Float? = null,
        var weather: List<DailyMetaDataWeather>? = null,
        var clouds: Int? = null,
        var pop: Float? = null,
        var uvi: Float? = null

){
}