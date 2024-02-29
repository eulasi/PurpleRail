package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.storage.CityStorage
import com.example.weatherapp.repository.WeatherRepository

class WeatherViewModelFactory(
    private val repository: WeatherRepository, private val cityStorage: CityStorage
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository, cityStorage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
