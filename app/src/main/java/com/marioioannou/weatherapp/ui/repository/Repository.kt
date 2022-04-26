package com.marioioannou.weatherapp.ui.repository

import com.marioioannou.weatherapp.api.RetrofitService
import com.marioioannou.weatherapp.api.model.WeatherModel
import retrofit2.Response

class Repository {

    suspend fun getWeather(): Response<WeatherModel> {
        return RetrofitService.api.getWeather(38.4624F,23.5948F,"fc533aa63fe3bf033a6d655a7a9237ec","metric")
    }

}