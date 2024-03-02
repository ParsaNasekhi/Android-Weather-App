package com.parsanasekhi.androidweatherapp.repository.current_weather

import com.parsanasekhi.androidweatherapp.db.remote.CurrentWeatherResponse
import com.parsanasekhi.androidweatherapp.db.remote.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class CurrentWeatherRepositoryImpl @Inject constructor(private val weatherApiService: WeatherApiService) :
    CurrentWeatherRepository {
    override suspend fun getCurrentWeather(
        lat: String,
        lon: String
    ): Flow<Response<CurrentWeatherResponse>> = flow {
        emit(weatherApiService.getCurrentWeather(lat, lon))
    }.flowOn(Dispatchers.IO)
}