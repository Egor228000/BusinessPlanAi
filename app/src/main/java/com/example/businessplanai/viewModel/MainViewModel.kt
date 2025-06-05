package com.example.businessplanai.viewModel

import android.content.Context
import android.util.Log
import androidx.activity.contextaware.ContextAware
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businessplanai.data.BusinessDao
import com.example.businessplanai.data.BusinessEnity
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.bouncycastle.crypto.params.Blake3Parameters.context
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val dao: BusinessDao) : ViewModel() {



    val businessList: StateFlow<List<BusinessEnity>> =
        dao.getAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteBusiness(id: Int) {
        viewModelScope.launch {
            dao.delete(id)
        }
    }


    private val _fabVisible = MutableStateFlow(false)
    val fabVisible: StateFlow<Boolean> = _fabVisible

    fun onScroll(scrollDelta: Float) {
        _fabVisible.value = scrollDelta < 0
    }
    val globalScrollState = mutableStateMapOf<String, LazyListState>()





}