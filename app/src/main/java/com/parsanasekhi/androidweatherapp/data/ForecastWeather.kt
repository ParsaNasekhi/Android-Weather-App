package com.parsanasekhi.androidweatherapp.data

class ForecastWeather(
    val cityName: String
) : ArrayList<ForecastWeather.Detail>() {
    open class Detail (
        open val temp: String,
        open val date: String,
        open val time: String,
        open val icon: String,
        open val minTemp: String,
        open val maxTemp: String,
        open val humidity: String,
        open val windSpeed: String,
        open val sunset: String,
        open val sunrise: String,
        open val description: String
    )
}
