package com.cloutstory.foshoweather.models

data class HourlyMetaData (
        var dt: Long? = null,
        var temp: Float? = null,
        var feels_like: Float? = null,
        var pressure: Int? = null,
        var humidity: Int? = null,
        var dew_point: Float? = null,
        var uvi: Float? = null,
        var clouds: Int? = null,
        var visibility: Int? = null,
        var wind_speed: Float? = null,
        var wind_deg: Int? = null,
        var wind_gust: Float? = null
) {

}