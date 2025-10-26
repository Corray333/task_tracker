package com.example.tasktracker.domain.repository

import com.example.tasktracker.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    fun getTasksByDate(startOfDay: Long, endOfDay: Long): Flow<List<Task>>
    fun searchTasks(query: String): Flow<List<Task>>
    fun getTasksByTag(tag: String): Flow<List<Task>>
    fun getTasksByDateAndSearch(startOfDay: Long, endOfDay: Long, query: String): Flow<List<Task>>

    suspend fun getTaskById(id: Long): Task?
    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteTaskById(id: Long)
}
