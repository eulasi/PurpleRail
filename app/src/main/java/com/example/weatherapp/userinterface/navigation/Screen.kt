package com.example.weatherapp.userinterface.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object WeatherSearch : Screen("weather_search", "Weather", Icons.Filled.Cloud)
    object About : Screen("about", "About", Icons.Filled.Info)
}
