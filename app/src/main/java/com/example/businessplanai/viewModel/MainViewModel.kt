package com.example.businessplanai.viewModel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businessplanai.data.BusinessDao
import com.example.businessplanai.data.BusinessEnity
import com.example.businessplanai.ui.theme.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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