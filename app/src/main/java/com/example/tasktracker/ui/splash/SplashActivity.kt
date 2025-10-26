package com.example.tasktracker.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tasktracker.MainActivity
import com.example.tasktracker.data.local.UserPreferencesManager
import com.example.tasktracker.ui.auth.LoginActivity
import com.example.tasktracker.ui.base.BaseLocaleActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseLocaleActivity() {

    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before calling super.onCreate()
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // Keep the splash screen visible for a brief moment while checking auth
        splashScreen.setKeepOnScreenCondition { true }

        // Check if user is logged in
        val isLoggedIn = userPreferencesManager.isLoggedIn()

        // Navigate to appropriate screen
        val intent = if (isLoggedIn) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
