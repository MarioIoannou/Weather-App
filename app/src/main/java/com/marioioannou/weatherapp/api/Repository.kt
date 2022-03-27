package com.marioioannou.weatherapp.api

import com.marioioannou.weatherapp.api.model.WeatherModel
import retrofit2.Call
import retrofit2.Response

class Repository {

    suspend fun getWeather(): Response<WeatherModel> {
        return RetrofitInstance.api.getWeather(38.4624F,23.5948F,"ba3a5c5eb56940101c62bf15b2352356","metric")
    }

}