package com.example.businessplanai.viewModel

import android.app.Application
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.businessplanai.AppDatabase
import com.example.businessplanai.BusinessDao
import com.example.businessplanai.BusinessEnity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val businessDao: BusinessDao = AppDatabase.getInstance(application).businessDao()

    val businessList: StateFlow<List<BusinessEnity>> =
        businessDao.getAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteBusiness(id: Int) {
        viewModelScope.launch {
            businessDao.delete(id)
        }
    }


    private val _fabVisible = MutableStateFlow(false)
    val fabVisible: StateFlow<Boolean> = _fabVisible

    fun onScroll(scrollDelta: Float) {
        _fabVisible.value = scrollDelta < 0
    }
    val globalScrollState = mutableStateMapOf<String, LazyListState>()
}