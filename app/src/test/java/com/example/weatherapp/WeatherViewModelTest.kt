package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.weatherapp.data.response.WeatherResponse
import com.example.weatherapp.data.storage.CityStorage
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.viewmodel.WeatherViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: WeatherRepository
    private lateinit var cityStorage: CityStorage
    private lateinit var viewModel: WeatherViewModel

    private val observer: Observer<WeatherResponse> = mockk(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        cityStorage = mockk()
        viewModel = WeatherViewModel(repository, cityStorage)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `getWeatherByCityName updates weatherData`() = runTest {
        val weatherResponse = WeatherResponse(/* Initialize with test data */)
        coEvery { repository.getWeatherByCityName(any(), any()) } returns weatherResponse
        coEvery { cityStorage.saveCityName(any()) } returns Unit

        viewModel.weatherData.observeForever(observer)
        viewModel.getWeatherByCityName("Test City", "Test API Key")

        coVerify { observer.onChanged(weatherResponse) }
        coVerify { cityStorage.saveCityName("Test City") }
    }

    @Test
    fun `getWeatherByCoordinates updates weatherData`() = runTest {
        val weatherResponse = WeatherResponse(/* Initialize with test data */)
        coEvery { repository.getWeatherByCoordinates(any(), any(), any()) } returns weatherResponse

        viewModel.weatherData.observeForever(observer)
        viewModel.getWeatherByCoordinates(0.0, 0.0, "Test API Key")

        coVerify { observer.onChanged(weatherResponse) }
    }
}
