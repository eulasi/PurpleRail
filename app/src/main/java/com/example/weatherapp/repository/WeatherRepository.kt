package com.example.weatherapp.repository

import com.example.weatherapp.data.response.WeatherResponse
import com.example.weatherapp.data.remote.WeatherApiService


class WeatherRepository(private val api: WeatherApiService) {
    suspend fun getWeatherByCityName(cityName: String, apiKey: String): WeatherResponse? {
        val response = api.getWeatherByCityName(cityName, apiKey)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    suspend fun getWeatherByCoordinates(lat: Double, lon: Double, apiKey: String): WeatherResponse? {
        val response = api.getWeatherByCoordinates(lat, lon, apiKey)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

}