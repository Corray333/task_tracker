package com.example.tasktracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.tasktracker.domain.model.Task

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["dateEpochMillis"]),
        Index(value = ["userId"])
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
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

fun Task.toEntity(userId: Long): TaskEntity {
    return TaskEntity(
        id = id,
        userId = userId,
        title = title,
        description = description,
        tags = tags,
        dateEpochMillis = dateEpochMillis,
        icon = icon,
        colorHex = colorHex,
        priority = priority
    )
}
