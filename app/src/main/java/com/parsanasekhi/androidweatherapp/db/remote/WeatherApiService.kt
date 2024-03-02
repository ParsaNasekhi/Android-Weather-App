package com.parsanasekhi.androidweatherapp.db.remote

import com.parsanasekhi.androidweatherapp.utills.API_KEY
import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query

interface WeatherApiService {

    @GET(ApiUrl.Weather)
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String = API_KEY
    ): Response<CurrentWeatherResponse>

}