package com.cloutstory.foshoweather.models.DailyDataModels

data class DailyMetaDataFeelsLike (
        var day: Float? = null,
        var night: Float? = null,
        var eve: Float? = null,
        var morn: Float? = null
) {
}