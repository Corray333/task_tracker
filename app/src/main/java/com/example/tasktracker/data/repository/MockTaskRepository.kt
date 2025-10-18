package com.example.tasktracker.data.repository

import com.example.tasktracker.domain.model.Task
import com.example.tasktracker.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockTaskRepository @Inject constructor() : TaskRepository {

    private val _tasks = MutableStateFlow(generateMockTasks())
    private val tasks = _tasks.asStateFlow()

    override fun getTasks(): Flow<List<Task>> = tasks

    override suspend fun getTaskById(id: Long): Task? {
        return _tasks.value.find { it.id == id }
    }

    override suspend fun insertTask(task: Task) {
        _tasks.value = _tasks.value + task
    }

    override suspend fun updateTask(task: Task) {
        _tasks.value = _tasks.value.map { if (it.id == task.id) task else it }
    }

    override suspend fun deleteTask(task: Task) {
        _tasks.value = _tasks.value.filter { it.id != task.id }
    }

    private fun generateMockTasks(): List<Task> {
        val currentTime = System.currentTimeMillis()
        return listOf(
            Task(
                id = 1L,
                title = "Complete Android project",
                description = "Finish the task tracker application with all features",
                tags = listOf("work", "urgent"),
                dateEpochMillis = currentTime + 86400000, // +1 day
                icon = "PhoneAndroid",
                colorHex = "#FF6B6B",
                priority = 1
            ),
            Task(
                id = 2L,
                title = "Buy groceries",
                description = "Milk, bread, eggs, vegetables",
                tags = listOf("personal", "shopping"),
                dateEpochMillis = currentTime + 3600000, // +1 hour
                icon = "ShoppingCart",
                colorHex = "#4ECDC4",
                priority = 2
            ),
            Task(
                id = 3L,
                title = "Gym workout",
                description = "Leg day: squats, lunges, leg press",
                tags = listOf("health", "fitness"),
                dateEpochMillis = currentTime + 7200000, // +2 hours
                icon = "FitnessCenter",
                colorHex = "#95E1D3",
                priority = 3
            ),
            Task(
                id = 4L,
                title = "Read Kotlin documentation",
                description = "Study coroutines and flows chapter",
                tags = listOf("learning", "programming"),
                dateEpochMillis = currentTime + 172800000, // +2 days
                icon = "MenuBook",
                colorHex = "#F38181",
                priority = 2
            ),
            Task(
                id = 5L,
                title = "Call dentist",
                description = null,
                tags = listOf("health", "appointments"),
                dateEpochMillis = currentTime + 259200000, // +3 days
                icon = "MedicalServices",
                colorHex = "#AA96DA",
                priority = 1
            ),
            Task(
                id = 6L,
                title = "Prepare presentation",
                description = "Create slides for Monday's meeting",
                tags = listOf("work", "presentation"),
                dateEpochMillis = currentTime + 432000000, // +5 days
                icon = "BarChart",
                colorHex = "#FCBAD3",
                priority = 1
            )
        )
    }
}
