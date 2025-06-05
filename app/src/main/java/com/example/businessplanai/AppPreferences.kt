package com.example.businessplanai

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.businessplanai.AppPreferences.PreferencesKeys.MODEL_PATH_KEY
import com.example.businessplanai.ui.theme.AppTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
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
        val MODEL_PATH_KEY = stringPreferencesKey("model_path")
    }
    suspend fun saveModelPath(path: String) {
        context.dataStore.edit { prefs ->
            prefs[MODEL_PATH_KEY] = path
        }
    }

    fun loadModelPath(): Flow<String?> {
        return context.dataStore.data
            .map { prefs: Preferences ->
                prefs[MODEL_PATH_KEY]
            }
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



    suspend fun getServerIp(): String {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.SERVER_IP] ?: ""
            }
            .first()
    }

}