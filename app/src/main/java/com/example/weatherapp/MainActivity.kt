package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
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

class MainActivity : ComponentActivity() {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request location permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        setContent {
            val navController = rememberNavController()
            val repository = WeatherRepository(WeatherApiDetails.api)
            val cityStorage = CityStorage(this)
            val viewModelFactory = WeatherViewModelFactory(repository, cityStorage)
            Scaffold(
                bottomBar = { BottomNavBar(navController) }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.WeatherSearch.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Screen.WeatherSearch.route) {
                        val viewModel: WeatherViewModel = viewModel(factory = viewModelFactory)
                        WeatherSearchScreen(viewModel)
                    }
                    composable(Screen.About.route) {
                        AboutPageScreen()
                    }
                }
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
            // Permissions granted, you can handle any additional logic here if needed
        }
    }
}

