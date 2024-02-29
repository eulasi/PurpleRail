package com.example.weatherapp.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherImageStorage(private val  context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("weatherImage")
        private val IMAGE_KEY = stringPreferencesKey("image_key")
    }
    val getImageKey: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[IMAGE_KEY] ?: ""
    }

    suspend fun saveImageKey(imageKey: String) {
        context.dataStore.edit { preferences ->
            preferences[IMAGE_KEY] = imageKey
        }
    }
}