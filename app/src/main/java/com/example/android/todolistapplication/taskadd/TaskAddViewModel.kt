package com.example.android.todolistapplication.taskadd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.todolistapplication.database.Task
import com.example.android.todolistapplication.database.TaskDatabaseDao
import kotlinx.coroutines.launch

class TaskAddViewModel(
    dataSource: TaskDatabaseDao
) : ViewModel() {

    val database = dataSource

    private val _navigateToTaskList = MutableLiveData<Boolean?>()
    val navigateToTaskList: LiveData<Boolean?>
        get() = _navigateToTaskList

    private val _submitButtonClicked = MutableLiveData<Boolean?>()
    val submitButtonClicked: LiveData<Boolean?>
        get() = _submitButtonClicked

    fun onCreateTask(title: String, description: String) {
        viewModelScope.launch {
            val newTask = Task()
            if (title != "" && description != "") {
                newTask.title = title
                newTask.description = description
            } else {
                if (title == "") {
                    newTask.title = "Empty title"
                }
                if (description == "") {
                    newTask.description = "Empty description"
                }
            }
            insert(newTask)
            _navigateToTaskList.value = true
        }
    }

    private suspend fun insert(task: Task) {
        database.insert(task)
    }

    fun onSubmit() {
        _submitButtonClicked.value = true
    }

    fun doneSubmit() {
        _submitButtonClicked.value = null
    }

    fun onClickBackButton() {
        _navigateToTaskList.value = true
    }

    fun doneNavigating() {
        _navigateToTaskList.value = null
    }

}
