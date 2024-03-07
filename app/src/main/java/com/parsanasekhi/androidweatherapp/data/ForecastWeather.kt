package com.parsanasekhi.androidweatherapp.data

class ForecastWeather : ArrayList<ForecastWeather.Detail>() {
    data class Detail (
        val temp: String,
        val date: String,
        val time: String,
        val icon: String,
        val minTemp: String,
        val maxTemp: String,
        val humidity: String,
        val windSpeed: String,
        val sunset: String,
        val sunrise: String,
        val description: String
    )
}
