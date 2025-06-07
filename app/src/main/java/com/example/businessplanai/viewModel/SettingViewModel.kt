package com.example.businessplanai.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businessplanai.AppPreferences
import com.example.businessplanai.ui.theme.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
) : ViewModel() {

    private val _appTheme = MutableStateFlow<AppTheme?>(null)
    val appTheme: StateFlow<AppTheme?> = _appTheme

    private val _serverIp = MutableStateFlow("")



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

    init {
        // Подписываемся на Flow из AppPreferences
        viewModelScope.launch {
            appPreferences.modelPathFlow.collect { savedPath ->
                _modelPath.value = savedPath
            }
        }
    }

    // Вызывается из UI (Compose) при выборе нового файла
    fun saveModelPath(path: String) {
        viewModelScope.launch {
            // Сохраняем в DataStore
            appPreferences.saveModelPath(path)
            _modelPath.value = path
        }
    }


}