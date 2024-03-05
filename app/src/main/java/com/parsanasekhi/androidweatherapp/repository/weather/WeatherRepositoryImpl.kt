package com.parsanasekhi.androidweatherapp.repository.weather

import com.parsanasekhi.androidweatherapp.db.remote.weather.response.CurrentWeatherResponse
import com.parsanasekhi.androidweatherapp.db.remote.weather.WeatherApiService
import com.parsanasekhi.androidweatherapp.db.remote.weather.response.ForecastWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherApiService: WeatherApiService) :
    WeatherRepository {
    override suspend fun getCurrentWeather(
        cityName: String
    ): Flow<Response<CurrentWeatherResponse>> = flow {
        emit(weatherApiService.getCurrentWeather(cityName))
    }.flowOn(Dispatchers.IO)

    override suspend fun getForecastWeather(
        cityName: String,
        daysCount: String
    ): Flow<Response<ForecastWeatherResponse>> = flow {
        emit(weatherApiService.getForecastWeather(cityName))
    }.flowOn(Dispatchers.IO)
}