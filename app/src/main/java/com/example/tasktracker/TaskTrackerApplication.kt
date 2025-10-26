package com.example.tasktracker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.lifecycleScope
import com.example.tasktracker.data.local.LanguagePreferencesManager
import com.example.tasktracker.util.LocaleManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class TaskTrackerApplication : Application() {

    @Inject
    lateinit var languagePreferencesManager: LanguagePreferencesManager

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val languageCode = runBlocking {
            languagePreferencesManager.languageCode.first()
        }
        LocaleManager.applyLocale(this, languageCode)
    }
}
