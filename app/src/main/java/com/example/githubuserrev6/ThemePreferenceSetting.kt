package com.example.githubuserrev6

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreferenceSetting private constructor(private val dataStore: DataStore<Preferences>) {

    private val themeKey = booleanPreferencesKey("theme_config")

    fun getThemeConfig(): Flow<Boolean> {
        return dataStore.data.map {
            it[themeKey] ?: false
        }
    }

    suspend fun saveThemeConfig(isDarkThemeActive: Boolean) {
        dataStore.edit {
            it[themeKey] = isDarkThemeActive
        }
    }

    companion object {
        @Volatile
        private var themeInstance: ThemePreferenceSetting? = null

        fun getInstance(dataStore: DataStore<Preferences>): ThemePreferenceSetting {
            return themeInstance ?: synchronized(this) {
                val themeInstance = ThemePreferenceSetting(dataStore)
                Companion.themeInstance = themeInstance
                themeInstance
            }
        }
    }
}