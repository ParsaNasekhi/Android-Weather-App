package com.parsanasekhi.androidweatherapp.utills

import androidx.compose.runtime.mutableStateOf
import com.parsanasekhi.androidweatherapp.data.City
import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.data.Location

const val API_KEY = "9393c0405b411e64971dc8f1e8dc8baf"

val EmptyLocation = Location("", "")
val EmptyCurrentWeather =
    CurrentWeather("", EmptyLocation, null, "", "", "", "", "", "", "", "", "", "", "", "")

val cityFromBookmarkScreen = mutableStateOf<City?>(null)
val removeCityEvent = mutableStateOf(false)
val needToUpdateBookmarkScreen = mutableStateOf(false)

const val BottomAppBarHeight = 100

enum class LoadState {
    LOADING,
    SUCCESS,
    ERROR,
    EMPTY
}

sealed class LoadResponse {
    data object Loading: LoadResponse()
    data class Failed(val message: String? = null) : LoadResponse()
    data class Success<T>(val data: T? = null): LoadResponse()
}