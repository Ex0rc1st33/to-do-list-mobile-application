package com.example.android.todolistapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDatabaseDao {

    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Query("DELETE FROM task_table WHERE id = :key")
    suspend fun deleteById(key: Long)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("SELECT * from task_table WHERE id = :key")
    suspend fun getById(key: Long): Task

    @Query("SELECT * FROM task_table ORDER BY is_done")
    fun getAllTasks(): LiveData<List<Task>>

}
