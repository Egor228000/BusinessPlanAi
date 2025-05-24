package com.example.businessplanai.viewModel


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businessplanai.data.BusinessDao
import com.example.businessplanai.data.BusinessEnity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditViewModel @Inject constructor(private val dao: BusinessDao) : ViewModel() {



    private val _business = MutableStateFlow<BusinessEnity?>(null)
    val business: StateFlow<BusinessEnity?> = _business


    // Временные значения для редактирования
    var editedTitle = mutableStateOf("")
    var editedDescription = mutableStateOf("")

    fun loadBusinessById(id: Int?) {
        viewModelScope.launch {
            val result = dao.getId(id)
            _business.value = result
            result?.let {
                editedTitle.value = it.title
                editedDescription.value = it.description
            }
        }
    }

    fun saveChanges() {
        val current = _business.value ?: return
        val updated = current.copy(
            title = editedTitle.value,
            description = editedDescription.value
        )

        viewModelScope.launch {
            dao.insert(updated)
        }
    }
}