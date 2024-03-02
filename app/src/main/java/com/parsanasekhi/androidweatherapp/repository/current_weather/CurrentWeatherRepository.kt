package com.parsanasekhi.androidweatherapp.repository.current_weather

import com.parsanasekhi.androidweatherapp.db.remote.CurrentWeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CurrentWeatherRepository {

    suspend fun getCurrentWeather(
        lat: String,
        lon: String
    ): Flow<Response<CurrentWeatherResponse>>

}