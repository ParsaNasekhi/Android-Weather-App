package com.parsanasekhi.androidweatherapp.repository.weather

import com.parsanasekhi.androidweatherapp.data.CurrentWeather
import com.parsanasekhi.androidweatherapp.data.ForecastWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeather(
        cityName: String
    ): Flow<CurrentWeather>

    suspend fun getForecastWeather(
        cityName: String,
        daysCount: String,
    ): Flow<ForecastWeather>

    suspend fun getCityWeatherById(
        id: Int
    ): Flow<CurrentWeather>

}