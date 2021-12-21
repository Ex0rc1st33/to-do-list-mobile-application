package com.example.android.todolistapplication.taskedit

import androidx.lifecycle.*
import com.example.android.todolistapplication.database.Task
import com.example.android.todolistapplication.database.TaskDatabaseDao
import kotlinx.coroutines.launch
import com.example.android.todolistapplication.convertCreationDateToStringWithLabel

class TaskEditViewModel(
    dataSource: TaskDatabaseDao,
    private val id: Long
) : ViewModel() {

    val database = dataSource

    private val _task = MutableLiveData<Task?>()
    val task: LiveData<Task?>
        get() = _task

    val taskCreationDateFormatted = Transformations.map(task) { task ->
        task?.let {
            convertCreationDateToStringWithLabel(it.creationDate, "Created at:")
        }
    }

    private val _navigateToTaskList = MutableLiveData<Boolean?>()
    val navigateToTaskList: LiveData<Boolean?>
        get() = _navigateToTaskList

    private val _editTask = MutableLiveData<Boolean?>()
    val editTask: LiveData<Boolean?>
        get() = _editTask

    private val _showDeleteToast = MutableLiveData<Boolean?>()
    val showDeleteToast: LiveData<Boolean?>
        get() = _showDeleteToast

    init {
        getCorrectTaskById()
    }

    private fun getCorrectTaskById() {
        viewModelScope.launch {
            _task.value = get()
        }
    }

    private suspend fun get(): Task {
        return database.getById(id)
    }

    fun onClickBackButton() {
        _navigateToTaskList.value = true
    }

    fun doneNavigating() {
        _navigateToTaskList.value = null
    }

    fun onEditTask() {
        if (_editTask.value == null) {
            _editTask.value = true
        } else if (_editTask.value == true) {
            _editTask.value = false
        }
    }

    fun doneEditTask(newTitle: String, newDescription: String) {
        viewModelScope.launch {
            update(newTitle, newDescription)
            _editTask.value = null
        }
    }

    private suspend fun update(newTitle: String, newDescription: String) {
        _task.value?.let {
            val newTask = Task(it.id, newTitle, newDescription, it.isDone)
            _task.value = newTask
            database.update(newTask)
        }
    }

    fun onDeleteTask() {
        viewModelScope.launch {
            delete()
            _showDeleteToast.value = true
            _navigateToTaskList.value = true
        }
    }

    private suspend fun delete() {
        database.deleteById(id)
    }

    fun doneShowDeleteToast() {
        _showDeleteToast.value = null
    }

}
