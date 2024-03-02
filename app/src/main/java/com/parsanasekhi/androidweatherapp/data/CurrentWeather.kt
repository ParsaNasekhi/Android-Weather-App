package com.parsanasekhi.androidweatherapp.data

data class CurrentWeather(
    val name: String,
    val location: Location,
    val description: String,
    val icon: String,
    val temp: String,
    val minTemp: String,
    val maxTemp: String,
    val sunrise: String,
    val sunset: String,
    val humidity: String,
    val windSpeed: String,
    val id: String
)
