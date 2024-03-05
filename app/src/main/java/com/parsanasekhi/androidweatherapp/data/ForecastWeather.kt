package com.parsanasekhi.androidweatherapp.data

class ForecastWeather : ArrayList<ForecastWeather.Detail>() {
    data class Detail (
        val temp: String,
        val date: String,
        val time: String,
        val icon: String
    )
}
