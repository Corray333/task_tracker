package com.example.tasktracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tasktracker.data.local.dao.UserDao
import com.example.tasktracker.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskTrackerDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "task_tracker_db"

        fun getDatabaseName(userId: Long?): String {
            return if (userId != null) {
                "${DATABASE_NAME}_user_$userId"
            } else {
                DATABASE_NAME
            }
        }
    }
}
