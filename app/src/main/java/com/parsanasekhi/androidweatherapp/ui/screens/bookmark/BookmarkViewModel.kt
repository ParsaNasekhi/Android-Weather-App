package com.parsanasekhi.androidweatherapp.ui.screens.bookmark

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parsanasekhi.androidweatherapp.data.City
import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.repository.bookmarked_city.BookmarkedCityRepository
import com.parsanasekhi.androidweatherapp.repository.weather.WeatherRepository
import com.parsanasekhi.androidweatherapp.utills.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkedCityRepository: BookmarkedCityRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _cities = MutableStateFlow(listOf<City>())

    private val _citiesWeather = mutableStateListOf<CurrentWeather>()
    val citiesWeather = _citiesWeather

    private val _citiesWeatherLoadState = MutableStateFlow(LoadState.LOADING)
    val citiesWeatherLoadState = _citiesWeatherLoadState.asStateFlow()

    init {
        getBookmarkedCitiesWeather()
    }

    fun unbookmarkCity(city: City) {
        viewModelScope.launch {
            bookmarkedCityRepository.deleteCity(city)
            getBookmarkedCitiesWeather()
        }
    }

    fun getBookmarkedCitiesWeather() {
        viewModelScope.launch {
            _citiesWeatherLoadState.value = LoadState.LOADING
            delay(1500)
            _cities.value = listOf()
            _citiesWeather.clear()
            getBookmarkedCities()
            _cities.value.forEach {
                getCityWeather(it.id)
            }
        }
    }

    private suspend fun getCityWeather(id: Int) {
        weatherRepository.getCityWeatherById(id = id)
            .onStart {
            }.catch { throwable ->
                _citiesWeatherLoadState.value = LoadState.ERROR
                Log.w("ManualLog", "getCityWeather/catch: ${throwable.message}")
            }.collectLatest { response ->
                _citiesWeather.add(response)
                _citiesWeatherLoadState.value = LoadState.SUCCESS
            }
    }

    private suspend fun getBookmarkedCities() {
        bookmarkedCityRepository.gelAllCities()
            .collectLatest {
                _cities.value = it
                if (it.isEmpty())
                    _citiesWeatherLoadState.value = LoadState.EMPTY
            }
    }

}