package com.parsanasekhi.androidweatherapp.db.remote.weather

import com.parsanasekhi.androidweatherapp.db.remote.ApiUrl
import com.parsanasekhi.androidweatherapp.db.remote.weather.response.CurrentWeatherResponse
import com.parsanasekhi.androidweatherapp.db.remote.weather.response.ForecastWeatherResponse
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

    @GET(ApiUrl.CurrentWeather)
    suspend fun getCityWeatherById(
        @Query("id") id: Int,
        @Query("appid") apiKey: String
        = API_KEY
    ): Response<CurrentWeatherResponse>
    @GET(ApiUrl.CurrentWeather)
    suspend fun getCityWeatherByLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String = API_KEY
    ): Response<CurrentWeatherResponse>

    @GET(ApiUrl.ForecastWeather)
    suspend fun getForecastWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = API_KEY
    ): Response<ForecastWeatherResponse>

}