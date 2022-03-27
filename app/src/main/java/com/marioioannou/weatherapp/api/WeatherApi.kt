package com.marioioannou.weatherapp.api

import com.marioioannou.weatherapp.api.model.WeatherModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat")lat:Float,
        @Query("lon")lon:Float,
        @Query("appid")apiKey:String,
        @Query("units")units:String
    ):Response<WeatherModel>
}