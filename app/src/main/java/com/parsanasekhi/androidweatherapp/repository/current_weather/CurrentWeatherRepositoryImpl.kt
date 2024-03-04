package com.parsanasekhi.androidweatherapp.repository.current_weather

import com.parsanasekhi.androidweatherapp.db.remote.current_weather.CurrentWeatherResponse
import com.parsanasekhi.androidweatherapp.db.remote.current_weather.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class CurrentWeatherRepositoryImpl @Inject constructor(private val weatherApiService: WeatherApiService) :
    CurrentWeatherRepository {
    override suspend fun getCurrentWeather(
        cityName: String
    ): Flow<Response<CurrentWeatherResponse>> = flow {
        emit(weatherApiService.getCurrentWeather(cityName))
    }.flowOn(Dispatchers.IO)
}