package com.parsanasekhi.androidweatherapp.data

data class CurrentWeather(
    val cityName: String,
    val location: Location,
    val cityId: Int?,
    override val description: String,
    override val icon: String,
    override val temp: String,
    override val minTemp: String,
    override val maxTemp: String,
    override val sunrise: String,
    override val sunset: String,
    override val humidity: String,
    override val windSpeed: String,
    override val time: String,
    override val date: String,
    val country: String
) : ForecastWeather.Detail(
    temp = temp,
    icon = icon,
    minTemp = minTemp,
    maxTemp = maxTemp,
    sunrise = sunrise,
    sunset = sunset,
    humidity = humidity,
    windSpeed = windSpeed,
    time = time,
    date = date,
    description = description
)
