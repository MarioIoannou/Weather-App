package com.marioioannou.weatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marioioannou.weatherapp.api.Repository
import com.marioioannou.weatherapp.api.model.WeatherModel
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val weatherResponse: MutableLiveData<Response<WeatherModel>> = MutableLiveData()

    fun getWeather(lat:Float,lon:Float,apiKey:String){
        viewModelScope.launch {
            val response = repository.getWeather()
            weatherResponse.value = response
        }
    }

}