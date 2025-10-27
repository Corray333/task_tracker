package com.example.tasktracker.data.repository

import com.example.tasktracker.data.local.UserPreferencesManager
import com.example.tasktracker.data.local.dao.TaskDao
import com.example.tasktracker.data.local.entity.toEntity
import com.example.tasktracker.data.local.entity.toTask
import com.example.tasktracker.domain.model.Task
import com.example.tasktracker.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomTaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val userPreferencesManager: UserPreferencesManager
) : TaskRepository {

    private fun getCurrentUserId(): Long {
        return userPreferencesManager.userId
            ?: throw IllegalStateException("User must be logged in")
    }

    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks(getCurrentUserId()).map { entities ->
            entities.map { it.toTask() }
        }
    }

    override fun getTasksByDate(startOfDay: Long, endOfDay: Long): Flow<List<Task>> {
        return taskDao.getTasksByDate(getCurrentUserId(), startOfDay, endOfDay).map { entities ->
            entities.map { it.toTask() }
        }
    }

    override fun searchTasks(query: String): Flow<List<Task>> {
        return taskDao.searchTasks(getCurrentUserId(), query).map { entities ->
            entities.map { it.toTask() }
        }
    }

    override fun getTasksByTag(tag: String): Flow<List<Task>> {
        return taskDao.getTasksByTag(getCurrentUserId(), tag).map { entities ->
            entities.map { it.toTask() }
        }
    }

    override fun getTasksByDateAndSearch(
        startOfDay: Long,
        endOfDay: Long,
        query: String
    ): Flow<List<Task>> {
        return taskDao.getTasksByDateAndSearch(getCurrentUserId(), startOfDay, endOfDay, query).map { entities ->
            entities.map { it.toTask() }
        }
    }

    override suspend fun getTaskById(id: Long): Task? {
        return taskDao.getTaskById(id, getCurrentUserId())?.toTask()
    }

    override suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task.toEntity(getCurrentUserId()))
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity(getCurrentUserId()))
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity(getCurrentUserId()))
    }

    override suspend fun deleteTaskById(id: Long) {
        taskDao.deleteTaskById(id)
    }
}
