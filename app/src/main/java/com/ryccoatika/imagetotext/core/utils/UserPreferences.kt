package com.ryccoatika.imagetotext.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {
    private val dataStore: DataStore<Preferences> by lazy {
        context.createDataStore("user_pref")
    }

    companion object {
        private val FIRST_TIME_KEY = preferencesKey<Boolean>("first_time")
    }

    suspend fun saveFirstTime(state: Boolean) {
        dataStore.edit { preferences ->
            preferences[FIRST_TIME_KEY] = state
        }
    }

    val isFirstTime: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[FIRST_TIME_KEY] ?: true
    }
}