package com.example.tasktracker.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    var userId: Long?
        get() = sharedPreferences.getLong(KEY_USER_ID, -1L).takeIf { it != -1L }
        set(value) {
            if (value != null) {
                sharedPreferences.edit().putLong(KEY_USER_ID, value).apply()
            } else {
                sharedPreferences.edit().remove(KEY_USER_ID).apply()
            }
        }

    var username: String?
        get() = sharedPreferences.getString(KEY_USERNAME, null)
        set(value) {
            if (value != null) {
                sharedPreferences.edit().putString(KEY_USERNAME, value).apply()
            } else {
                sharedPreferences.edit().remove(KEY_USERNAME).apply()
            }
        }

    fun isLoggedIn(): Boolean = userId != null

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "task_tracker_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
    }
}
