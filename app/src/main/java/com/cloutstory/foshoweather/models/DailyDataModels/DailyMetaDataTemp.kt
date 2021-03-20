package com.cloutstory.foshoweather.models.DailyDataModels

data class DailyMetaDataTemp (
        var day: Float? = null,
        var min: Float? = null,
        var max: Float? = null,
        var night: Float? = null,
        var eve: Float? = null,
        var morn: Float? = null
) {
}