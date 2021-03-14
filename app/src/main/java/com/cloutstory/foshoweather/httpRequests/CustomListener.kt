package com.cloutstory.foshoweather.httpRequests

interface CustomListener<T> {
    fun getResult(result: T)
}