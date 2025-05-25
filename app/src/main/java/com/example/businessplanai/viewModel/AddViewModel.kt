package com.example.businessplanai.viewModel

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
        ipAdress:  String
    ): String {

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
                                    content = """
                                        Сгенерируй подробный бизнес-план, последовательно раскрывая 5 ключевых вопросов. На основе ответов составь структурированный документ с анализом, стратегиями и расчетами.
                                        И сделай таблицу с помощью markdown к Финансовуму плану.
                                        1. Название вашего бизнеса: $nameBusiness
                                        2. Суть бизнеса: $pointBusiness
                                        (Чем будет заниматься компания? Кратко опиши продукт или услугу.)
                                        3. Целевая аудитория: $auditoriumBusiness
                                        (Кто основные клиенты? Их возраст, доход, потребности.)
                                        4. Конкурентные преимущества: $advantagesBusiness
                                        "(Почему клиенты выберут именно этот бизнес? Уникальные особенности.)
                                        5. Модель монетизации: $monetizationBusiness
                                        (Как будет зарабатывать? Основные источники дохода, ценообразование.)
                                        6. Барьеры и решения: $barriersAndSolutionsBusiness
                                        (Какие главные риски? Как их минимизировать?)
                                        Дополнительные разделы (сформируй их на основе ответов выше):
                                        Анализ рынка (ниша, тренды, конкуренты).
                                        Маркетинг и продвижение (каналы, бюджет).
                                        Операционная деятельность (локация, оборудование, логистика).
                                        Финансовый план (инвестиции, расходы, окупаемость).
                                        KPI (ключевые метрики успеха)
                                    """.trimIndent().toString()
                                )
                            ),
                            temperature = 0.1,
                            max_tokens = 5000

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