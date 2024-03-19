package com.parsanasekhi.androidweatherapp.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parsanasekhi.androidweatherapp.data.City
import com.parsanasekhi.androidweatherapp.data.ForecastWeather
import com.parsanasekhi.androidweatherapp.repository.bookmarked_city.BookmarkedCityRepository
import com.parsanasekhi.androidweatherapp.repository.weather.WeatherRepository
import com.parsanasekhi.androidweatherapp.utills.EmptyCurrentWeather
import com.parsanasekhi.androidweatherapp.utills.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val bookmarkedCityRepository: BookmarkedCityRepository
) : ViewModel() {

    private val _currentWeather = MutableStateFlow(EmptyCurrentWeather)
    val currentWeather = _currentWeather.asStateFlow()
    private val _currentWeatherLoadState = MutableStateFlow(LoadState.LOADING)
    val currentWeatherLoadState = _currentWeatherLoadState.asStateFlow()

    private val _forecastWeather = MutableStateFlow<List<ForecastWeather.Detail>>(listOf())
    val forecastWeather = _forecastWeather.asStateFlow()
    private val _forecastWeatherLoadState = MutableStateFlow(LoadState.LOADING)
    val forecastWeatherLoadState = _forecastWeatherLoadState.asStateFlow()

    private val _isCityBookmarked = MutableStateFlow(false)
    val isCityBookmarked = _isCityBookmarked.asStateFlow()

    init {
        getCurrentWeather("Mashhad")
        getForecastWeather("Mashhad", "5")
    }

    fun bookmarkCity(city: City) {
        viewModelScope.launch {
            bookmarkedCityRepository.insertCity(city)
            _isCityBookmarked.value = true
        }
    }

    fun unbookmarkCity(city: City) {
        viewModelScope.launch {
            bookmarkedCityRepository.deleteCity(city)
            _isCityBookmarked.value = false
        }
    }

    suspend fun checkIsCityBookmarked(id: Int) {
        bookmarkedCityRepository.checkCityExist(id)
            .collectLatest {
                _isCityBookmarked.value = it
            }
    }

    fun getForecastWeather(cityName: String, daysCount: String) {
        viewModelScope.launch {
            weatherRepository.getForecastWeather(cityName, daysCount)
                .onStart {
                    _forecastWeatherLoadState.value = LoadState.LOADING
                }.catch { throwable ->
                    if (throwable.message == "Not Found" || throwable.message == "Bad Request" || throwable.message == null)
                        _forecastWeatherLoadState.value = LoadState.EMPTY
                    else
                        _currentWeatherLoadState.value = LoadState.ERROR
                    Log.i("ManualLog", "getForecastWeather/catch: ${throwable.message}")
                }.collect { forecastWeather ->
                    _forecastWeather.value = forecastWeather.toList()
                    _forecastWeatherLoadState.value = LoadState.SUCCESS
                }

        }
    }

    fun getCurrentWeather(cityName: String) {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather(cityName = cityName)
                .onStart {
                    _currentWeatherLoadState.value = LoadState.LOADING
                }.catch { throwable ->
                    if (throwable.message == "Not Found" || throwable.message == "Bad Request" || throwable.message == null)
                        _currentWeatherLoadState.value = LoadState.EMPTY
                    else
                        _currentWeatherLoadState.value = LoadState.ERROR
                    Log.i("ManualLog", "getCurrentWeather/catch: ${throwable.message}")
                }.collect { currentWeather ->
                    checkIsCityBookmarked(currentWeather.cityId!!)
                    _currentWeather.value = currentWeather
                    _currentWeatherLoadState.value = LoadState.SUCCESS
                }
        }
    }

    fun getWeatherByCityId(cityId: Int) {
        viewModelScope.launch {
            weatherRepository.getCityWeatherById(cityId)
                .onStart {
                    _currentWeatherLoadState.value = LoadState.LOADING
                }.catch { throwable ->
                    if (throwable.message == "Not Found")
                        _currentWeatherLoadState.value = LoadState.EMPTY
                    else
                        _currentWeatherLoadState.value = LoadState.ERROR
                    Log.w("ManualLog", "getCurrentWeatherByCityId/catch: ${throwable.message}")
                }.collectLatest { response ->
                    checkIsCityBookmarked(response.cityId!!)
                    getForecastWeather(response.cityName, "5")
                    _currentWeather.value = response
                    _currentWeatherLoadState.value = LoadState.SUCCESS
                }
        }
    }

}