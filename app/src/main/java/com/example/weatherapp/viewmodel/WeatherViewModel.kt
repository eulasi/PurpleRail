package com.example.weatherapp.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.response.WeatherResponse
import com.example.weatherapp.data.storage.CityStorage
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository, private val cityStorage: CityStorage) : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?> = _location

    fun getWeatherByCityName(cityName: String, apiKey: String) {
        viewModelScope.launch {
            val data = repository.getWeatherByCityName(cityName, apiKey)
            data?.let {
                _weatherData.postValue(it)
                cityStorage.saveCityName(cityName)
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

    fun updateLocation(location: Location) {
        _location.postValue(location)
    }
}
