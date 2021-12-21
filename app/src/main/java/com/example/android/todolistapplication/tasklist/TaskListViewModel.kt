package com.example.android.todolistapplication.tasklist

import androidx.lifecycle.*
import com.example.android.todolistapplication.database.Task
import com.example.android.todolistapplication.database.TaskDatabaseDao
import com.example.android.todolistapplication.network.WeatherApi
import kotlinx.coroutines.launch

class TaskListViewModel(
    dataSource: TaskDatabaseDao
) : ViewModel() {

    val database = dataSource

    val tasks = database.getAllTasks()

    private val _navigateToTaskAdd = MutableLiveData<Boolean?>()
    val navigateToTaskAdd: LiveData<Boolean?>
        get() = _navigateToTaskAdd

    private val _navigateToTaskEdit = MutableLiveData<Long?>()
    val navigateToTaskEdit: LiveData<Long?>
        get() = _navigateToTaskEdit

    private val _showDeleteAllToast = MutableLiveData<Boolean?>()
    val showDeleteAllToast: LiveData<Boolean?>
        get() = _showDeleteAllToast

    val clearAllButtonVisible = Transformations.map(tasks) {
        it.isNotEmpty()
    }

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    init {
        getWeatherProperty()
    }

    fun onAddNewTask() {
        _navigateToTaskAdd.value = true
    }

    fun doneNavigatingToTaskAdd() {
        _navigateToTaskAdd.value = null
    }

    fun onClearAllTasks() {
        viewModelScope.launch {
            delete()
            _showDeleteAllToast.value = true
        }
    }

    private suspend fun delete() {
        database.deleteAll()
    }

    fun doneShowDeleteAllToast() {
        _showDeleteAllToast.value = null
    }

    fun onToggleCheckbox(id: Long) {
        viewModelScope.launch {
            val task = get(id)
            task.isDone = !task.isDone
            update(task)
        }
    }

    private suspend fun get(id: Long): Task {
        return database.getById(id)
    }

    private suspend fun update(task: Task) {
        database.update(task)
    }

    fun onClickLayout(id: Long) {
        _navigateToTaskEdit.value = id
    }

    fun doneNavigatingToTaskEdit() {
        _navigateToTaskEdit.value = null
    }

    private fun getWeatherProperty() {
        viewModelScope.launch {
            try {
                val result = WeatherApi.retrofitService.getWeatherInfo()
                _response.value = "Current weather in Debrecen: ${result.current.condition.text}"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }

}
