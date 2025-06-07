package com.example.businessplanai.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businessplanai.NetworkStatusTracker
import com.example.businessplanai.data.BusinessDao
import com.example.businessplanai.data.BusinessEnity
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val dao: BusinessDao,
    networkStatusTracker: NetworkStatusTracker,
) : ViewModel() {


    private val _response = MutableStateFlow("")


    val isConnected = networkStatusTracker.observeNetworkStatus()
        .distinctUntilChanged()
        .onStart { emit(true) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(100),
            initialValue = true
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isLoadingNavigate = MutableStateFlow<Boolean?>(null)
    val isLoadingNavigate: StateFlow<Boolean?> = _isLoadingNavigate

    private val _isModelReady = MutableStateFlow(false)
    val isModelReady: StateFlow<Boolean> = _isModelReady

    private var llmInference: LlmInference? = null

    fun initLlm(context: Context, modelPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val taskOptions = LlmInferenceOptions.builder()
                    .setModelPath(modelPath)
                    .setMaxTopK(64)
                    .setMaxTokens(4096)
                    .setPreferredBackend(LlmInference.Backend.CPU)
                    .build()

                llmInference = LlmInference.createFromOptions(context, taskOptions)
                Log.d("LLM_INIT", "LLM успешно инициализирована по пути: $modelPath")
                _isModelReady.value = true

            } catch (e: Exception) {
                Log.e("LLM_INIT", "Не удалось инициализировать LLM по пути: $modelPath", e)
                _isModelReady.value = false
            }
        }
    }


    fun generateAndSaveToDb(
        prompt: String,
        title: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoadingNavigate.value = true

            _isLoading.value = true
            try {
                val result = llmInference?.generateResponse(prompt) ?: "Подкючите модель в настройках"
                _response.value = result

                dao.insert(
                    BusinessEnity(
                        title = title,
                        description = result
                    )
                )
                _isLoadingNavigate.value = false

            } catch (e: Exception) {
                _response.value = "Error: ${e.message}"
            } finally {
                _isLoadingNavigate.value = false

                _isLoading.value = false
            }
        }
    }
}

