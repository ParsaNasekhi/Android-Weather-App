package com.parsanasekhi.androidweatherapp.db.remote.geocoding

import com.parsanasekhi.androidweatherapp.db.remote.ApiUrl
import com.parsanasekhi.androidweatherapp.utills.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {

    @GET(ApiUrl.Geocoding)
    suspend fun getLocation(
        @Query("q") cityName: String = "Mashhad",
        @Query("appid") apiKey: String = API_KEY
    ): Response<GeocodingResponse>

}