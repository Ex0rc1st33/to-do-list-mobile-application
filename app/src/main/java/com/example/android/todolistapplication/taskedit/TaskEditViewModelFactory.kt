package com.example.android.todolistapplication.taskedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.todolistapplication.database.TaskDatabaseDao

class TaskEditViewModelFactory(
    private val dataSource: TaskDatabaseDao,
    private val id: Long
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskEditViewModel::class.java)) {
            return TaskEditViewModel(dataSource, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
