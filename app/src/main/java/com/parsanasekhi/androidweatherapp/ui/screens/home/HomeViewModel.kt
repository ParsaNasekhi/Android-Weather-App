package com.parsanasekhi.androidweatherapp.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.data.ForecastWeather
import com.parsanasekhi.androidweatherapp.data.Location
import com.parsanasekhi.androidweatherapp.repository.geocoding.GeocodingRepository
import com.parsanasekhi.androidweatherapp.repository.weather.WeatherRepository
import com.parsanasekhi.androidweatherapp.utills.EmptyCurrentWeather
import com.parsanasekhi.androidweatherapp.utills.LoadState
import com.parsanasekhi.androidweatherapp.utills.formatUnixTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val geocodingRepository: GeocodingRepository
) : ViewModel() {

    private val _currentWeather = MutableStateFlow(EmptyCurrentWeather)
    val currentWeather = _currentWeather.asStateFlow()
    private val _currentWeatherLoadState = MutableStateFlow(LoadState.LOADING)
    val currentWeatherLoadState = _currentWeatherLoadState.asStateFlow()

    private val _forecastWeather = MutableStateFlow<List<ForecastWeather.Detail>>(listOf())
    val forecastWeather = _forecastWeather.asStateFlow()
    private val _forecastWeatherLoadState = MutableStateFlow(LoadState.LOADING)
    val forecastWeatherLoadState = _forecastWeatherLoadState.asStateFlow()

    init {
        getCurrentWeather("Mashhad")
        getForecastWeather("Mashhad", "5")
    }

    fun getForecastWeather(cityName: String, daysCount: String) {
        viewModelScope.launch {
            weatherRepository.getForecastWeather(cityName, daysCount)
                .onStart {
                    _forecastWeatherLoadState.value = LoadState.LOADING
                }.catch { throwable ->
                    _forecastWeatherLoadState.value = LoadState.ERROR
                    Log.w("ManualLog", "getForecastWeather/catch: ${throwable.message}")
                }.filter { response ->
                    if (!response.isSuccessful) {
                        Log.w("ManualLog", "getForecastWeather/filter: ${response.message()}")
                        _currentWeatherLoadState.value = LoadState.ERROR
                    }
                    response.isSuccessful && response.body() != null
                }.map { response ->
                    val forecastWeather = ForecastWeather()
                    response.body()!!.list.forEach {
                        forecastWeather.add(
                            ForecastWeather.Detail(
                                temp = (it.main.temp - 273.15).roundToInt().toString(),
                                date = formatUnixTime(it.dt.toString(), "MMM dd"),
                                time = formatUnixTime(it.dt.toString(), "hh:mm a"),
                                icon = it.weather[0].icon
                            )
                        )
                    }
                    forecastWeather
                }.collectLatest { forecastWeather ->
                    _forecastWeather.value = forecastWeather.toList()
                    _forecastWeatherLoadState.value = LoadState.SUCCESS
                }
        }
    }

    fun getCurrentWeather(cityName: String) {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather(
                cityName = cityName
            ).onStart {
                _currentWeatherLoadState.value = LoadState.LOADING
            }.catch { throwable ->
                _currentWeatherLoadState.value = LoadState.ERROR
                Log.w("ManualLog", "getCurrentWeather/catch: ${throwable.message}")
            }.filter { response ->
                if (!response.isSuccessful || response.body() == null) {
                    _currentWeatherLoadState.value = LoadState.ERROR
                    Log.w("ManualLog", "getCurrentWeather/catch: ${response.message()}")
                }
                response.isSuccessful && response.body() != null
            }.map { response ->
                CurrentWeather(
                    name = response.body()!!.name,
                    location = Location(
                        lat = response.body()!!.coord.lat.toString(),
                        lon = response.body()!!.coord.lon.toString()
                    ),
                    icon = response.body()!!.weather[0].icon,
                    description = response.body()!!.weather[0].description,
                    temp = (response.body()!!.main.temp - 273.15).roundToInt().toString(),
                    minTemp = (response.body()!!.main.tempMin - 273.15).roundToInt().toString(),
                    maxTemp = (response.body()!!.main.tempMax - 273.15).roundToInt().toString(),
                    humidity = "${response.body()!!.main.humidity}%",
                    id = response.body()!!.id.toString(),
                    sunset = formatUnixTime(response.body()!!.sys.sunset.toString(), "hh:mm a"),
                    sunrise = formatUnixTime(response.body()!!.sys.sunrise.toString(), "hh:mm a"),
                    windSpeed = "${response.body()!!.wind.speed} m/s"
                )
            }.collect { response ->
                _currentWeather.value = response
                _currentWeatherLoadState.value = LoadState.SUCCESS
            }
        }
    }

}