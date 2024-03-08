package com.parsanasekhi.androidweatherapp.ui.screens.bookmark

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parsanasekhi.androidweatherapp.data.City
import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.repository.bookmarked_city.BookmarkedCityRepository
import com.parsanasekhi.androidweatherapp.repository.weather.WeatherRepository
import com.parsanasekhi.androidweatherapp.utills.EmptyCurrentWeather
import com.parsanasekhi.androidweatherapp.utills.LoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
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

    fun unbookmarkCity(cityId: Int) {

    }

    fun getBookmarkedCitiesWeather() {
        viewModelScope.launch {
            _cities.value = listOf()
            _citiesWeather.clear()
            getBookmarkedCities()
            Log.i("TestLog", "getBookmarkedCitiesWeather: ${_cities.value}")
            _cities.value.forEach {
                getCityWeather(it.id)
            }
        }
    }

    private suspend fun getCityWeather(id: Int) {
        weatherRepository.getCityWeatherById(id = id)
            .onStart {
//                    _currentWeatherLoadState.value = LoadState.LOADING
            }.catch { throwable ->
//                    _currentWeatherLoadState.value = LoadState.ERROR
                Log.w("ManualLog", "getCityWeather/catch: ${throwable.message}")
            }.collectLatest { response ->
                _citiesWeather.add(response)
//                    _currentWeatherLoadState.value = LoadState.SUCCESS
            }
    }

    private suspend fun getBookmarkedCities() {
        bookmarkedCityRepository.gelAllCities()
            .collectLatest {
                _cities.value = it
            }
    }

}