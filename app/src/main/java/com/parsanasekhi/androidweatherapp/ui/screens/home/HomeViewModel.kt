package com.parsanasekhi.androidweatherapp.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.data.Location
import com.parsanasekhi.androidweatherapp.repository.current_weather.CurrentWeatherRepository
import com.parsanasekhi.androidweatherapp.repository.geocoding.GeocodingRepository
import com.parsanasekhi.androidweatherapp.utills.EmptyCurrentWeather
import com.parsanasekhi.androidweatherapp.utills.EmptyLocation
import com.parsanasekhi.androidweatherapp.utills.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val geocodingRepository: GeocodingRepository
) : ViewModel() {

    private val _currentWeather = MutableStateFlow(EmptyCurrentWeather)
    val currentWeather = _currentWeather.asStateFlow()
    private val _currentWeatherLoadState = MutableStateFlow(LoadState.LOADING)
    val currentWeatherLoadState = _currentWeatherLoadState.asStateFlow()

    private val _cityLocation = MutableStateFlow(EmptyLocation)

    //    val cityLocation = _cityLocation.asStateFlow()
    private val _cityLocationLoadState = MutableStateFlow(LoadState.LOADING)
    val cityLocationLoadState = _cityLocationLoadState.asStateFlow()

    fun getCurrentWeatherFromCityName(cityName: String) {
        viewModelScope.launch {
            getLocationFromCityName(cityName)
            getCurrentWeather(_cityLocation.value)
        }
    }

    private suspend fun getCurrentWeather(location: Location) {
        currentWeatherRepository.getCurrentWeather(
            lat = location.lat,
            lon = location.lon
        ).onStart {
            _currentWeatherLoadState.value = LoadState.LOADING
        }.catch { throwable ->
            _currentWeatherLoadState.value = LoadState.ERROR
            Log.w("ManualLog", "getCurrentWeather: ${throwable.message}")
        }.filter { response ->
            if (!response.isSuccessful)
                _currentWeatherLoadState.value = LoadState.ERROR
            response.isSuccessful
        }.map { response ->
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
        }.collect { response ->
            _currentWeatherLoadState.value = LoadState.SUCCESS
            _currentWeather.value = response
        }
    }

    private suspend fun getLocationFromCityName(cityName: String) {
        geocodingRepository.getLocation(cityName)
            .onStart {
                _cityLocationLoadState.value = LoadState.LOADING
            }.catch {
                _cityLocationLoadState.value = LoadState.ERROR
            }.filter { response ->
                if (!response.isSuccessful)
                    _cityLocationLoadState.value = LoadState.ERROR
                response.isSuccessful && response.body()!!.size > 0 && response.body()!![0].localNames?.en == cityName
            }.map { response ->
                Location(
                    lat = response.body()!![0].lat.toString(),
                    lon = response.body()!![0].lon.toString()
                )
            }.collect { location ->
                _cityLocation.value = location
                _cityLocationLoadState.value = LoadState.SUCCESS
            }
    }

}