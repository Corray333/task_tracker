package com.example.tasktracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tasktracker.data.local.dao.TaskDao
import com.example.tasktracker.data.local.dao.UserDao
import com.example.tasktracker.data.local.entity.TaskEntity
import com.example.tasktracker.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, TaskEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TaskTrackerDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "task_tracker_db"
    }
}
