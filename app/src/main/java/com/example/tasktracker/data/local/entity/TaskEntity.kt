package com.example.tasktracker.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.tasktracker.domain.model.Task

@Entity(
    tableName = "tasks",
    indices = [Index(value = ["dateEpochMillis"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String?,
    val tags: List<String>,
    val dateEpochMillis: Long,
    val icon: String?,
    val colorHex: String?,
    val priority: Int
)

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        tags = tags,
        dateEpochMillis = dateEpochMillis,
        icon = icon,
        colorHex = colorHex,
        priority = priority
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        tags = tags,
        dateEpochMillis = dateEpochMillis,
        icon = icon,
        colorHex = colorHex,
        priority = priority
    )
}
