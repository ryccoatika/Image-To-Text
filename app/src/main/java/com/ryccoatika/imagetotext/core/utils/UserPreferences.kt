package com.ryccoatika.imagetotext.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_pref")

    companion object {
        private val FIRST_TIME_KEY = booleanPreferencesKey("first_time")
    }

    suspend fun saveFirstTime(state: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[FIRST_TIME_KEY] = state
        }
    }

    val isFirstTime: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[FIRST_TIME_KEY] ?: true
    }
}