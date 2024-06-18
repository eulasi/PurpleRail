package com.example.weatherapp.mocks

import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.model.Clouds
import com.example.weatherapp.data.model.Coord
import com.example.weatherapp.data.model.Main
import com.example.weatherapp.data.model.Sys
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.model.Wind
import com.example.weatherapp.data.remote.WeatherApiService
import com.example.weatherapp.data.response.*
import com.example.weatherapp.data.storage.CityStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class MockWeatherApiService : WeatherApiService {
    override suspend fun getWeatherByCityName(cityName: String, apiKey: String): Response<WeatherResponse> {
        val weatherResponse = WeatherResponse(
            base = "stations",
            clouds = Clouds(all = 0),
            cod = 200,
            coord = Coord(lat = 37.7749, lon = -122.4194),
            dt = 1605182400,
            id = 5391959,
            main = Main(
                temp = 15.0,
                feelsLike = 14.0,
                tempMin = 13.0,
                tempMax = 16.0,
                pressure = 1013,
                humidity = 72
            ),
            name = "San Francisco",
            sys = Sys(
                type = 1,
                id = 5817,
                country = "US",
                sunrise = 1605166197,
                sunset = 1605202860
            ),
            timezone = -28800,
            visibility = 10000,
            weather = listOf(
                Weather(
                    id = 801,
                    main = "Clouds",
                    description = "few clouds",
                    icon = "02d"
                )
            ),
            wind = Wind(speed = 1.5, deg = 350)
        )
        return Response.success(weatherResponse)
    }

    override suspend fun getWeatherByCoordinates(lat: Double, lon: Double, apiKey: String): Response<WeatherResponse> {
        val weatherResponse = WeatherResponse(
            base = "stations",
            clouds = Clouds(all = 0),
            cod = 200,
            coord = Coord(lat = 37.7749, lon = -122.4194),
            dt = 1605182400,
            id = 5391959,
            main = Main(
                temp = 15.0,
                feelsLike = 14.0,
                tempMin = 13.0,
                tempMax = 16.0,
                pressure = 1013,
                humidity = 72
            ),
            name = "San Francisco",
            sys = Sys(
                type = 1,
                id = 5817,
                country = "US",
                sunrise = 1605166197,
                sunset = 1605202860
            ),
            timezone = -28800,
            visibility = 10000,
            weather = listOf(
                Weather(
                    id = 801,
                    main = "Clouds",
                    description = "few clouds",
                    icon = "02d"
                )
            ),
            wind = Wind(speed = 1.5, deg = 350)
        )
        return Response.success(weatherResponse)
    }
}
