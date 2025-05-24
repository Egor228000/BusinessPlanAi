package com.example.businessplanai

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.businessplanai.ui.theme.AppTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "app_preferences")

    private object PreferencesKeys {
        val APP_THEME = stringPreferencesKey("app_theme")
        val SERVER_IP = stringPreferencesKey("server_ip")
    }

    suspend fun saveAppTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_THEME] = theme.name
        }
    }

    suspend fun getAppTheme(): AppTheme {
        return context.dataStore.data
            .map { preferences ->
                try {
                    AppTheme.valueOf(preferences[PreferencesKeys.APP_THEME] ?: AppTheme.SYSTEM.name)
                } catch (_: Exception) {
                    AppTheme.SYSTEM
                }
            }
            .first()
    }

    suspend fun saveServerIp(ip: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SERVER_IP] = ip
        }
    }

    suspend fun getServerIp(): String {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.SERVER_IP] ?: ""
            }
            .first()
    }
}