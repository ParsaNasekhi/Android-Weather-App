package com.parsanasekhi.androidweatherapp.db.remote.current_weather

import com.parsanasekhi.androidweatherapp.db.remote.ApiUrl
import com.parsanasekhi.androidweatherapp.utills.API_KEY
import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query

interface WeatherApiService {

    @GET(ApiUrl.CurrentWeather)
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = API_KEY
    ): Response<CurrentWeatherResponse>

}