package com.rachidbs.todo.network

import com.rachidbs.todo.tasklist.Task
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class TasksRepository {
    private val webService = Api.INSTANCE.tasksWebService

    suspend fun loadTasks(): List<Task>? {
        val response = webService.getTasks()
        return if (response.isSuccessful) response.body()!! else null
    }

    suspend fun updateTask(task: Task): Boolean {
        val response = webService.updateTask(task, task.id)
        return response.isSuccessful
    }

    suspend fun createTask(task: Task): Boolean {
        val response = webService.createTask(task)
        return response.isSuccessful
    }

    suspend fun deleteTask(task: Task): Boolean {
        val response = webService.deleteTask(task.id)
        return response.isSuccessful
    }
}