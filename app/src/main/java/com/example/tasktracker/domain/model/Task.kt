package com.example.tasktracker.domain.model

data class Task(
    val id: Long = 0L,
    val title: String,
    val description: String?,
    val tags: List<String>,
    val dateEpochMillis: Long,
    val icon: String?,
    val colorHex: String?,
    val priority: Int
)
