package com.example.weatherapp.mocks

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Clouds
import com.example.weatherapp.data.model.Coord
import com.example.weatherapp.data.model.Main
import com.example.weatherapp.data.model.Sys
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.model.Wind
import com.example.weatherapp.data.response.*
import com.example.weatherapp.data.storage.CityStorage
import com.example.weatherapp.data.storage.WeatherImageStorage
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utilities.Constants
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MockWeatherViewModel : WeatherViewModel(
    repository = WeatherRepository(MockWeatherApiService()),
    cityStorage = object : CityStorage(LocalContext.current) {
        override val getCityName: Flow<String> = flowOf("San Francisco")
        override suspend fun saveCityName(cityName: String) {}
    }
) {
    init {
        val mockWeatherResponse = WeatherResponse(
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
        _weatherData.value = mockWeatherResponse
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun WeatherSearchScreen(viewModel: WeatherViewModel) {
    val context = LocalContext.current
    val cityStorage = remember { CityStorage(context) }
    val cityNameState = cityStorage.getCityName.collectAsState(initial = "")
    var cityName by remember { mutableStateOf(cityNameState.value) }

    val weatherData = viewModel.weatherData.observeAsState().value

    val weatherImageStorage = remember { WeatherImageStorage(context) }
    val imageKey = weatherImageStorage.getImageKey.collectAsState(initial = "").value

    val imageUrl = imageKey.ifEmpty { "https://openweathermap.org/img/w/${weatherData?.weather?.get(0)?.icon}.png" }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.weather_wp),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier
            .padding(top = 30.dp, start = 26.dp, end = 26.dp)
        ) {
            if (weatherData != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = rememberImagePainter(imageUrl),
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(130.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = weatherData.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Temperature: ${weatherData.main.temp}°C",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Currently ${weatherData.weather[0].main}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = weatherData.weather[0].description,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            TextField(
                value = cityName,
                onValueChange = {
                    cityName = it
                    CoroutineScope(Dispatchers.IO).launch {
                        cityStorage.saveCityName(it)
                    }
                },
                label = { Text("Enter city name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.getWeatherByCityName(cityName, Constants.WEATHER_API_KEY) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Search")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherSearchScreenPreview() {
    val viewModel = MockWeatherViewModel()
    WeatherSearchScreen(viewModel = viewModel)
}
