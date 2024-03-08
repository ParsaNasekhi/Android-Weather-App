package com.parsanasekhi.androidweatherapp.utills

import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.data.Location

const val API_KEY = "9393c0405b411e64971dc8f1e8dc8baf"

val EmptyLocation = Location("", "")
val EmptyCurrentWeather =
    CurrentWeather("", EmptyLocation, null, "", "", "", "", "", "", "", "", "", "", "", "")
