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
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _appTheme = MutableStateFlow<AppTheme?>(null)
    val appTheme: StateFlow<AppTheme?> = _appTheme

    private val _serverIp = MutableStateFlow("")
    val serverIp: StateFlow<String> = _serverIp



    init {
        viewModelScope.launch {
            _appTheme.value = appPreferences.getAppTheme()
        }
    }

    fun setTheme(theme: AppTheme) {
        _appTheme.value = theme
        viewModelScope.launch {
            appPreferences.saveAppTheme(theme)
        }
    }

    fun setServerIp(ip: String) {
        _serverIp.value = ip
        viewModelScope.launch {
            appPreferences.saveServerIp(ip)
        }
    }
}