package com.example.tasktracker.di

import android.content.Context
import androidx.room.Room
import com.example.tasktracker.data.local.TaskTrackerDatabase
import com.example.tasktracker.data.local.UserPreferencesManager
import com.example.tasktracker.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTaskTrackerDatabase(
        @ApplicationContext context: Context,
        userPreferencesManager: UserPreferencesManager
    ): TaskTrackerDatabase {
        val userId = userPreferencesManager.userId
        val dbName = TaskTrackerDatabase.getDatabaseName(userId)

        return Room.databaseBuilder(
            context,
            TaskTrackerDatabase::class.java,
            dbName
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: TaskTrackerDatabase): UserDao {
        return database.userDao()
    }
}
