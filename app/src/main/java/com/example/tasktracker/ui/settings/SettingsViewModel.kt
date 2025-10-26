package com.example.tasktracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker.data.local.LanguagePreferencesManager
import com.example.tasktracker.data.local.ThemePreferencesManager
import com.example.tasktracker.data.local.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val username: String? = null,
    val isLoggingOut: Boolean = false,
    val loggedOut: Boolean = false,
    val selectedLanguage: String = LanguagePreferencesManager.LANGUAGE_SYSTEM,
    val selectedTheme: String = ThemePreferencesManager.THEME_SYSTEM
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager,
    private val languagePreferencesManager: LanguagePreferencesManager,
    private val themePreferencesManager: ThemePreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadUserInfo()
        loadLanguagePreference()
        loadThemePreference()
    }

    private fun loadUserInfo() {
        _uiState.value = _uiState.value.copy(
            username = userPreferencesManager.username
        )
    }

    private fun loadLanguagePreference() {
        viewModelScope.launch {
            languagePreferencesManager.languageCode.collect { language ->
                _uiState.value = _uiState.value.copy(
                    selectedLanguage = language
                )
            }
        }
    }

    private fun loadThemePreference() {
        viewModelScope.launch {
            themePreferencesManager.themeMode.collect { theme ->
                _uiState.value = _uiState.value.copy(
                    selectedTheme = theme
                )
            }
        }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            languagePreferencesManager.setLanguage(languageCode)
        }
    }

    fun setTheme(themeMode: String) {
        viewModelScope.launch {
            themePreferencesManager.setTheme(themeMode)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoggingOut = true)
            userPreferencesManager.logout()
            _uiState.value = _uiState.value.copy(
                isLoggingOut = false,
                loggedOut = true
            )
        }
    }
}
