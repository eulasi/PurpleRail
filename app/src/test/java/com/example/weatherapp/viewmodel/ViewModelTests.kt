package com.example.weatherapp.viewmodel

import com.example.weatherapp.data.response.WeatherResponse
import com.example.weatherapp.data.storage.CityStorage
import org.junit.Assert.*

import org.junit.Assert.assertEquals
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utilities.LiveDataTestUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class ViewModelTests {

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getWeatherByCityName is called, weatherData is updated`() = runTest {
        val repository = mock<WeatherRepository>()
        val cityStorage = mock<CityStorage>()
        val viewModel = WeatherViewModel(repository, cityStorage)
        val fakeWeatherData = WeatherResponse()
        val observedData = LiveDataTestUtil.getValue(viewModel.weatherData)
        whenever(repository.getWeatherByCityName(any(), any())).thenReturn(fakeWeatherData)

        viewModel.getWeatherByCityName("Test City", "API_KEY")

        assertEquals(fakeWeatherData, observedData)
    }

}

