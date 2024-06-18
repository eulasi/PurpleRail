package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.remote.WeatherApiDetails
import com.example.weatherapp.data.storage.CityStorage
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.userinterface.composables.AboutPageScreen
import com.example.weatherapp.userinterface.composables.WeatherSearchScreen
import com.example.weatherapp.userinterface.navigation.BottomNavBar
import com.example.weatherapp.userinterface.navigation.Screen
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.WeatherViewModelFactory
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val TAG = "MainActivity"
    }

    private lateinit var locationManager: LocationManager
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                Log.d(TAG, "Permissions granted: $granted")
                if (granted) {
                    fetchLocation()
                }
            }

        setContent {
            val navController = rememberNavController()
            val repository = WeatherRepository(WeatherApiDetails.api)
            val cityStorage = CityStorage(this)
            val viewModelFactory = WeatherViewModelFactory(repository, cityStorage)
            val viewModel: WeatherViewModel = viewModel(factory = viewModelFactory)

            Scaffold(
                bottomBar = { BottomNavBar(navController) }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.WeatherSearch.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Screen.WeatherSearch.route) {
                        WeatherSearchScreen(viewModel)
                        CheckLocationPermissionsAndFetchWeather(viewModel)
                    }
                    composable(Screen.About.route) {
                        AboutPageScreen()
                    }
                }
            }
        }
    }

    @Composable
    private fun CheckLocationPermissionsAndFetchWeather(viewModel: WeatherViewModel) {
        val context = this@MainActivity

        val location by viewModel.location.observeAsState()

        LaunchedEffect(location) {
            location?.let { loc ->
                Log.d(TAG, "Fetching weather for coordinates: ${loc.latitude}, ${loc.longitude}")
                viewModel.getWeatherByCoordinates(loc.latitude, loc.longitude, "YOUR_API_KEY")
            }
        }

        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            } else {
                fetchLocation()
            }
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            val lastKnownLocation: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            Log.d(TAG, "Fetched last known location: $lastKnownLocation")
            val viewModel: WeatherViewModel = ViewModelProvider(this, WeatherViewModelFactory(WeatherRepository(WeatherApiDetails.api), CityStorage(this))).get(WeatherViewModel::class.java)
            if (lastKnownLocation != null) {
                viewModel.updateLocation(lastKnownLocation)
            } else {
                Log.d(TAG, "Requesting location updates")
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    object : LocationListener {
                        override fun onLocationChanged(newLocation: Location) {
                            Log.d(TAG, "New location: $newLocation")
                            viewModel.updateLocation(newLocation)
                            locationManager.removeUpdates(this)
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                        override fun onProviderEnabled(provider: String) {}
                        override fun onProviderDisabled(provider: String) {}
                    }
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        }
    }
}
