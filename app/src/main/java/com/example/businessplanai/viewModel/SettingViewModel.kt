package com.example.businessplanai.viewModel

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businessplanai.AppPreferences
import com.example.businessplanai.ui.theme.AppTheme
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import androidx.core.net.toUri
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val client: HttpClient,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _appTheme = MutableStateFlow<AppTheme?>(null)
    val appTheme: StateFlow<AppTheme?> = _appTheme

    private val _serverIp = MutableStateFlow("")
    val serverIp: StateFlow<String> = _serverIp



    init {
        viewModelScope.launch {
            _appTheme.value = appPreferences.getAppTheme()
            _serverIp.value = appPreferences.getServerIp()
        }
    }

    fun setTheme(theme: AppTheme) {
        _appTheme.value = theme
        viewModelScope.launch {
            appPreferences.saveAppTheme(theme)
        }
    }





    private val _modelPath = MutableStateFlow<String?>(null)
    val modelPath: StateFlow<String?> = _modelPath

    private val _llmInference = MutableStateFlow<LlmInference?>(null)
    val llmInference: StateFlow<LlmInference?> = _llmInference

    private val _status = MutableStateFlow<String>("Модель не загружена")
    val status: StateFlow<String> = _status

    init {
        viewModelScope.launch {
            // Подписываемся на поток с путем к модели из AppPreferences
            appPreferences.loadModelPath().collect { path ->
                if (path != null) {
                    loadAndInitializeModel(path)
                } else {
                    _status.value = "Модель не выбрана"
                }
            }
        }
    }

    private suspend fun loadAndInitializeModel(path: String) {
        _status.value = "Загрузка модели..."
        try {
            val options = LlmInferenceOptions.builder()
                .setModelPath(path)
                .setMaxTopK(64)
                .setMaxTokens(5000)
                .build()

            val inference = withContext(Dispatchers.IO) {
                LlmInference.createFromOptions(context, options)
            }

            _llmInference.value = inference
            _modelPath.value = path
            _status.value = "Модель успешно загружена"
        } catch (e: Exception) {
            _status.value = "Ошибка загрузки модели: ${e.message}"
            Log.e("SettingsViewModel", "Error loading model", e)
        }
    }

    fun saveModelPath(path: String) {
        viewModelScope.launch {
            // Сохраняем путь через AppPreferences
            appPreferences.saveModelPath(path)
        }
    }


}