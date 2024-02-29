package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.response.WeatherResponse
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

    fun getWeatherByCityName(cityName: String, apiKey: String) {
        viewModelScope.launch {
            val data = repository.getWeatherByCityName(cityName, apiKey)
            data?.let {
                _weatherData.postValue(it)
            }
        }
    }

    fun getWeatherByCoordinates(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            val data = repository.getWeatherByCoordinates(lat, lon, apiKey)
            data?.let {
                _weatherData.postValue(it)
            }
        }
    }

}
