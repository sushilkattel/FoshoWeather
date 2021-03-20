package com.cloutstory.foshoweather.models

import com.cloutstory.foshoweather.models.DailyDataModels.DailyMetaDataFeelsLike
import com.cloutstory.foshoweather.models.DailyDataModels.DailyMetaDataTemp
import com.cloutstory.foshoweather.models.DailyDataModels.DailyMetaDataWeather

data class DailyMetaData (
        var dt: Long? = null,
        var sunrise: Long? = null,
        var sunset: Long? = null,
        var temp: List<DailyMetaDataTemp>? = null,
        var feels_like: List<DailyMetaDataFeelsLike>,
        var pressure: Int? = null,
        var humidity: Int? = null,
        var dew_point: Float? = null,
        var wind_speed: Float? = null,
        var wind_deg: Float? = null,
        var weather: Array<DailyMetaDataWeather>? = null,
        var clouds: Int? = null,
        var pop: Int? = null,
        var uvi: Float? = null

){
}