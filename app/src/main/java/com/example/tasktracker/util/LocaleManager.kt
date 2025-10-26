package com.example.tasktracker.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.example.tasktracker.data.local.LanguagePreferencesManager
import java.util.Locale

object LocaleManager {

    fun applyLocale(context: Context, languageCode: String): Context {
        val locale = when (languageCode) {
            LanguagePreferencesManager.LANGUAGE_ENGLISH -> Locale.ENGLISH
            LanguagePreferencesManager.LANGUAGE_RUSSIAN -> Locale("ru")
            else -> getSystemLocale(context)
        }

        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    private fun getSystemLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
    }

    fun getLocaleFromLanguageCode(languageCode: String, context: Context): Locale {
        return when (languageCode) {
            LanguagePreferencesManager.LANGUAGE_ENGLISH -> Locale.ENGLISH
            LanguagePreferencesManager.LANGUAGE_RUSSIAN -> Locale("ru")
            else -> getSystemLocale(context)
        }
    }
}
