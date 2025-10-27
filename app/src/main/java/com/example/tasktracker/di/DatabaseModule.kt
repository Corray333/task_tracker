package com.example.tasktracker.di

import android.content.Context
import androidx.room.Room
import com.example.tasktracker.data.local.TaskTrackerDatabase
import com.example.tasktracker.data.local.dao.TaskDao
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
        @ApplicationContext context: Context
    ): TaskTrackerDatabase {
        return Room.databaseBuilder(
            context,
            TaskTrackerDatabase::class.java,
            TaskTrackerDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: TaskTrackerDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: TaskTrackerDatabase): TaskDao {
        return database.taskDao()
    }
}
