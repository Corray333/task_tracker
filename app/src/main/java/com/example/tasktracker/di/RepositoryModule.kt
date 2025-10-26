package com.example.tasktracker.di

import com.example.tasktracker.data.repository.AuthRepositoryImpl
import com.example.tasktracker.data.repository.RoomTaskRepository
import com.example.tasktracker.domain.repository.AuthRepository
import com.example.tasktracker.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        roomTaskRepository: RoomTaskRepository
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
