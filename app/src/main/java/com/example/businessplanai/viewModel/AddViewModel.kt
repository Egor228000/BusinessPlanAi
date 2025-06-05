package com.example.businessplanai.viewModel

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businessplanai.data.BusinessDao
import com.example.businessplanai.data.BusinessEnity
import com.example.businessplanai.NetworkStatusTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import com.example.businessplanai.R
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class AddViewModel @Inject constructor(
    private val dao: BusinessDao, private val client: HttpClient,
    networkStatusTracker: NetworkStatusTracker,
) : ViewModel() {





    private var llmInference: LlmInference? = null

    private val _response = MutableStateFlow("")
    val response: StateFlow<String> = _response



    fun initLlm(context: Context) {
        viewModelScope.launch {
            val taskOptions = LlmInferenceOptions.builder()
                .setModelPath("/data/local/tmp/llm/qween0_5.task") // путь к модели
                .setMaxTopK(64)
                .build()

            llmInference = LlmInference.createFromOptions(context, taskOptions)
        }

    }

    fun generate(prompt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = llmInference?.generateResponse(prompt)
                Log.d("LLM", "result: $result")
                _response.value = result ?: "No response"
            } catch (e: Exception) {
                _response.value = "Error: ${e.message}"
            }
        }
    }



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

    private val _isLoadingNavigate = MutableStateFlow(false)
    val isLoadingNavigate: StateFlow<Boolean> = _isLoadingNavigate

    fun initLlm(context: Context, modelPath: String) {
        viewModelScope.launch {
            try {
                Log.d("LLM_INIT", "Using model path: $modelPath")

                val taskOptions = LlmInferenceOptions.builder()
                    .setModelPath(modelPath)
                    .setMaxTopK(64)
                    .build()

                llmInference = LlmInference.createFromOptions(context, taskOptions)

                Log.d("LLM_INIT", "LLM successfully initialized")

            } catch (e: Exception) {
                Log.e("LLM_INIT", "Failed to initialize LLM", e)
            }
        }
    }

    fun generateAndSaveToDb(
        prompt: String,
        title: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val result = llmInference?.generateResponse(prompt) ?: "No response"
                _response.value = result

                dao.insert(
                    BusinessEnity(
                        title = title,
                        description = result
                    )
                )
                _isLoadingNavigate.value = true
            } catch (e: Exception) {
                _response.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

@Serializable
data class ChatMessage(
    val role: String,
    val content: String
)

@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val temperature: Double,
    val max_tokens: Int
)