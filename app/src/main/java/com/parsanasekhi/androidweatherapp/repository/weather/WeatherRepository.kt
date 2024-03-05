package com.parsanasekhi.androidweatherapp.repository.weather

import com.parsanasekhi.androidweatherapp.db.remote.weather.response.CurrentWeatherResponse
import com.parsanasekhi.androidweatherapp.db.remote.weather.response.ForecastWeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRepository {

    suspend fun getCurrentWeather(
        cityName: String
    ): Flow<Response<CurrentWeatherResponse>>

    suspend fun getForecastWeather(
        cityName: String,
        daysCount: String,
    ): Flow<Response<ForecastWeatherResponse>>

}