package com.example.weatherapp.mocks

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockCityStorage(context: Context) {
    private val mockCityName = MutableLiveData("San Francisco")

    val getCityName: Flow<String> = flowOf("San Francisco")

    suspend fun saveCityName(cityName: String) {
        mockCityName.postValue(cityName)
    }
}
