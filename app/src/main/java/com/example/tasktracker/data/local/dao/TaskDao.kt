package com.example.tasktracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tasktracker.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY dateEpochMillis DESC, priority DESC")
    fun getAllTasks(userId: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId AND userId = :userId")
    suspend fun getTaskById(taskId: Long, userId: Long): TaskEntity?

    @Query("""
        SELECT * FROM tasks
        WHERE userId = :userId
        AND dateEpochMillis >= :startOfDay
        AND dateEpochMillis < :endOfDay
        ORDER BY priority DESC, dateEpochMillis ASC
    """)
    fun getTasksByDate(userId: Long, startOfDay: Long, endOfDay: Long): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks
        WHERE userId = :userId
        AND (title LIKE '%' || :searchQuery || '%'
        OR description LIKE '%' || :searchQuery || '%')
        ORDER BY dateEpochMillis DESC, priority DESC
    """)
    fun searchTasks(userId: Long, searchQuery: String): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks
        WHERE userId = :userId
        AND tags LIKE '%' || :tag || '%'
        ORDER BY dateEpochMillis DESC, priority DESC
    """)
    fun getTasksByTag(userId: Long, tag: String): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks
        WHERE userId = :userId
        AND dateEpochMillis >= :startOfDay
        AND dateEpochMillis < :endOfDay
        AND (title LIKE '%' || :searchQuery || '%'
        OR description LIKE '%' || :searchQuery || '%')
        ORDER BY priority DESC, dateEpochMillis ASC
    """)
    fun getTasksByDateAndSearch(
        userId: Long,
        startOfDay: Long,
        endOfDay: Long,
        searchQuery: String
    ): Flow<List<TaskEntity>>

    @Insert
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
}
