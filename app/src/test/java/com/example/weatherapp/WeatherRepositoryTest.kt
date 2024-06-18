package com.example.weatherapp.repository

import com.example.weatherapp.data.remote.WeatherApiService
import com.example.weatherapp.data.response.WeatherResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class WeatherRepositoryTest {

    private lateinit var apiService: WeatherApiService
    private lateinit var repository: WeatherRepository

    @Before
    fun setUp() {
        apiService = mockk()
        repository = WeatherRepository(apiService)
    }

    @Test
    fun `getWeatherByCityName returns WeatherResponse on success`() = runTest {
        val weatherResponse = WeatherResponse(/* Initialize with test data */)
        coEvery { apiService.getWeatherByCityName(any(), any()) } returns Response.success(weatherResponse)

        val result = repository.getWeatherByCityName("Test City", "Test API Key")

        assertNotNull(result)
        assertEquals(weatherResponse, result)
    }

    @Test
    fun `getWeatherByCityName returns null on failure`() = runTest {
        coEvery { apiService.getWeatherByCityName(any(), any()) } returns Response.error(404, mockk(relaxed = true))

        val result = repository.getWeatherByCityName("Test City", "Test API Key")

        assertNull(result)
    }

    @Test
    fun `getWeatherByCoordinates returns WeatherResponse on success`() = runTest {
        val weatherResponse = WeatherResponse(/* Initialize with test data */)
        coEvery { apiService.getWeatherByCoordinates(any(), any(), any()) } returns Response.success(weatherResponse)

        val result = repository.getWeatherByCoordinates(0.0, 0.0, "Test API Key")

        assertNotNull(result)
        assertEquals(weatherResponse, result)
    }

    @Test
    fun `getWeatherByCoordinates returns null on failure`() = runTest {
        coEvery { apiService.getWeatherByCoordinates(any(), any(), any()) } returns Response.error(404, mockk(relaxed = true))

        val result = repository.getWeatherByCoordinates(0.0, 0.0, "Test API Key")

        assertNull(result)
    }
}
