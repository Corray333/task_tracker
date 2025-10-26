package com.example.tasktracker.ui.base

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.tasktracker.data.local.LanguagePreferencesManager
import com.example.tasktracker.data.local.ThemePreferencesManager
import com.example.tasktracker.ui.theme.TaskTrackerTheme
import com.example.tasktracker.util.LocaleManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

abstract class BaseLocaleActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val languageCode = runBlocking {
            try {
                LanguagePreferencesManager(newBase).languageCode.first()
            } catch (e: Exception) {
                LanguagePreferencesManager.LANGUAGE_SYSTEM
            }
        }
        val context = LocaleManager.applyLocale(newBase, languageCode)
        super.attachBaseContext(context)
    }

    @Composable
    protected fun ThemedContent(content: @Composable () -> Unit) {
        val context = LocalContext.current
        val themePreferencesManager = ThemePreferencesManager(context)
        val themeMode by themePreferencesManager.themeMode.collectAsState(initial = ThemePreferencesManager.THEME_SYSTEM)

        val darkTheme = when (themeMode) {
            ThemePreferencesManager.THEME_LIGHT -> false
            ThemePreferencesManager.THEME_DARK -> true
            else -> androidx.compose.foundation.isSystemInDarkTheme()
        }

        TaskTrackerTheme(darkTheme = darkTheme) {
            content()
        }
    }
}
