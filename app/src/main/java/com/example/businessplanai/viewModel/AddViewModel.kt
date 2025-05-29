package com.example.businessplanai.viewModel

import android.content.res.Resources
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

@HiltViewModel
class AddViewModel @Inject constructor(
    private val dao: BusinessDao, private val client: HttpClient,
    networkStatusTracker: NetworkStatusTracker,
) : ViewModel() {

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


    suspend fun getFullChatResponse(
        nameBusiness: String,
        pointBusiness: String,
        auditoriumBusiness: String,
        advantagesBusiness: String,
        monetizationBusiness: String,
        barriersAndSolutionsBusiness: String,
        ipAdress:  String,
        resources: Resources  // ← Добавляем сюда ресурсы
    ): String {
        val prompt = resources.getString(
            R.string.business_plan_template,
            nameBusiness,
            pointBusiness,
            auditoriumBusiness,
            advantagesBusiness,
            monetizationBusiness,
            barriersAndSolutionsBusiness
        ).trimIndent()

        _isLoading.value = true
        return try {

            val response: HttpResponse =
                client.request("http://$ipAdress/v1/chat/completions") {

                    method = HttpMethod.Post
                    contentType(ContentType.Application.Json)
                    setBody(
                        ChatRequest(
                            model = "",
                            messages = listOf(
                                ChatMessage(
                                    role = "user",
                                    content = prompt
                                )
                            ),
                            temperature = 0.1,
                            max_tokens = 500

                        )
                    )
                }

            val responseBody = response.bodyAsText()
            val content = Json.parseToJsonElement(responseBody)
                .jsonObject["choices"]?.jsonArray?.firstOrNull()
                ?.jsonObject?.get("message")?.jsonObject?.get("content")?.jsonPrimitive?.content
                ?: "Бот не дал ответа"
            _isLoading.value = false
            _isLoadingNavigate.value = true

            // ✅ Сохраняем результат в БД
            val entity = BusinessEnity(
                title = nameBusiness,
                description = content
            )
            dao.insert(entity)
            _isLoadingNavigate.value = false
            content

        } catch (e: Exception) {
            e.printStackTrace()
            "Произошла ошибка: ${e.localizedMessage ?: "Неизвестная ошибка"}"
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