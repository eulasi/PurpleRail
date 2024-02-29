package com.example.weatherapp.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CityStorage(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("cityName")
        private val CITY_KEY = stringPreferencesKey("city_key")
    }

    val getCityName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CITY_KEY] ?: ""
    }

    suspend fun saveCityName(cityName: String) {
        context.dataStore.edit { preferences ->
            preferences[CITY_KEY] = cityName
        }
    }
}
