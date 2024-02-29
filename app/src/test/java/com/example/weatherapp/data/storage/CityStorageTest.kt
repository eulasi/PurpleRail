package com.example.weatherapp.data.storage


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CityStorageTest {

    private lateinit var cityStorage: CityStorage
    private lateinit var dataStore: DataStore<Preferences>

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataStore = context.preferencesDataStore(name = "test_datastore")
        cityStorage = CityStorage(dataStore)
    }

    @Test
    fun saveAndRetrieveCityName() = runTest {
        val testCityName = "Test City"

        // Save the city name
        cityStorage.saveCityName(testCityName)

        // Retrieve the city name
        val retrievedCityName = cityStorage.getCityName.first()

        // Verify that the retrieved city name is the same as the one saved
        assertEquals(testCityName, retrievedCityName)
    }
}
