package com.marioioannou.weatherapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marioioannou.weatherapp.api.model.WeatherModel
import com.marioioannou.weatherapp.ui.repository.Repository
import com.marioioannou.weatherapp.utils.ScreenState
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    private var _weatherResponse = MutableLiveData<ScreenState<Response<WeatherModel>>>()
    val weatherResponse: LiveData<ScreenState<Response<WeatherModel>>> = _weatherResponse

    init {
        getWeather()
    }

    private fun getWeather(){
        _weatherResponse.postValue(ScreenState.Loading(null))
        viewModelScope.launch {
           try{
               val response = repository.getWeather()
               if (response.isSuccessful){
                   _weatherResponse.postValue(ScreenState.Success(response))
               }else{
                   _weatherResponse.postValue(ScreenState.Error(null, response.message().toString()))
               }
           }catch (e: Throwable){
               when(e){
                   is IOException -> _weatherResponse.postValue(ScreenState.Error(null,"Network Failure"))
                   else -> _weatherResponse.postValue(ScreenState.Error(null,"Error"))
               }
           }
        }
    }
}