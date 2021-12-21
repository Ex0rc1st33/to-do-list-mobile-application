package com.example.android.todolistapplication.taskadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.todolistapplication.database.TaskDatabaseDao

class TaskAddViewModelFactory(
    private val dataSource: TaskDatabaseDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskAddViewModel::class.java)) {
            return TaskAddViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
