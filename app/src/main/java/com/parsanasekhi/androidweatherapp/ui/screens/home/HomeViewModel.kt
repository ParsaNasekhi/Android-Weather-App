package com.parsanasekhi.androidweatherapp.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.data.Location
import com.parsanasekhi.androidweatherapp.repository.current_weather.CurrentWeatherRepository
import com.parsanasekhi.androidweatherapp.utills.EmptyCurrentWeather
import com.parsanasekhi.androidweatherapp.utills.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val currentWeatherRepository: CurrentWeatherRepository) :
    ViewModel() {

    private val _currentWeather = MutableStateFlow(EmptyCurrentWeather)
    val currentWeather = _currentWeather.asStateFlow()
    private val _loadState = MutableStateFlow(LoadState.LOADING)
    val loadState = _loadState.asStateFlow()

    init {
        getCurrentWeather(Location("44.34", "10.99"))
    }

    private fun getCurrentWeather(location: Location) {
        viewModelScope.launch {
            currentWeatherRepository.getCurrentWeather(
                lat = location.lat,
                lon = location.lon
            ).onStart {
                _loadState.value = LoadState.LOADING
            }.catch { throwable ->
                _loadState.value = LoadState.ERROR
                Log.e("ManualLog", "getCurrentWeather: ${throwable.message}")
            }.map { response ->
                if (response.isSuccessful) {
                    _loadState.value = LoadState.SUCCESS
                    CurrentWeather(
                        name = response.body()!!.name,
                        location = Location(
                            lat = response.body()!!.coord.lat.toString(),
                            lon = response.body()!!.coord.lon.toString()
                        ),
                        icon = response.body()!!.weather[0].icon,
                        description = response.body()!!.weather[0].description,
                        temp = response.body()!!.main.temp.toString(),
                        minTemp = response.body()!!.main.tempMin.toString(),
                        maxTemp = response.body()!!.main.tempMax.toString(),
                        humidity = response.body()!!.main.humidity.toString(),
                        id = response.body()!!.id.toString(),
                        sunset = response.body()!!.sys.sunset.toString(),
                        sunrise = response.body()!!.sys.sunrise.toString(),
                        windSpeed = response.body()!!.wind.speed.toString()
                    )
                }
                else {
                    _loadState.value = LoadState.ERROR
                    EmptyCurrentWeather
                }
            }.collectLatest { response ->
                _currentWeather.value = response
            }
        }
    }

}