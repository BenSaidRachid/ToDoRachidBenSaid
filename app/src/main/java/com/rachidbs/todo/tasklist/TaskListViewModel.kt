package com.rachidbs.todo.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rachidbs.todo.network.TasksRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class TaskListViewModel : ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList

    fun loadTasks() {
        viewModelScope.launch {
            _taskList.value = repository.loadTasks()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            val isSuccessful = repository.updateTask(task)
            if (isSuccessful) {
                val tasks = _taskList.value.orEmpty().toMutableList()
                val position = tasks.indexOfFirst { it.id == task.id }

                tasks[position] = task
                _taskList.value = tasks
            }
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            val isSuccessful = repository.createTask(task)
            if (isSuccessful) {
                val tasks = _taskList.value.orEmpty().toMutableList()
                _taskList.value = tasks
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val isSuccessful = repository.deleteTask(task)
            if (isSuccessful) {
                val tasks = _taskList.value.orEmpty().toMutableList()
                tasks.remove(task)
                _taskList.value = tasks
            }
        }
    }
}