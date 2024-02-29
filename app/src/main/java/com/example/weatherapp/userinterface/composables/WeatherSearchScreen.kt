package com.example.weatherapp.userinterface.composables

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.weatherapp.viewmodel.WeatherViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import coil.annotation.ExperimentalCoilApi
import com.example.weatherapp.R
import com.example.weatherapp.data.storage.CityStorage
import com.example.weatherapp.data.storage.WeatherImageStorage
import com.example.weatherapp.utilities.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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


    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    viewModel.getWeatherByCoordinates(
                        it.latitude,
                        it.longitude,
                        Constants.WEATHER_API_KEY
                    )
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.weather_wp),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Adjust the scaling to fit or fill the screen
        )

        Column(modifier = Modifier.padding(top = 30.dp, start = 26.dp, end = 26.dp)) {

            if (weatherData != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {

                    Image(
                        painter = rememberImagePainter(imageUrl),
                        contentDescription = "Weather Icon",
                        modifier = Modifier
                            .size(130.dp)

                    )

                    Spacer(modifier = Modifier.width(16.dp))
                    Column {

                        Text(
                            text = "${weatherData.name}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Temperature: ${weatherData.main?.temp}°C",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Currently ${weatherData.weather?.get(0)?.main}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${weatherData.weather?.get(0)?.description}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            LaunchedEffect(weatherData?.weather?.get(0)?.icon) {
                weatherData?.weather?.get(0)?.icon?.let { icon ->
                    val newImageKey = "https://openweathermap.org/img/w/$icon.png"
                    weatherImageStorage.saveImageKey(newImageKey)
                }
            }
            LaunchedEffect(cityNameState.value) {
                cityName = cityNameState.value
            }

            TextField(
                value = cityName,
                onValueChange = {
                    cityName = it
                    // Save the city name asynchronously
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
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Search")
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
