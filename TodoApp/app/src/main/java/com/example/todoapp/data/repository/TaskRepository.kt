package com.example.todoapp.data.repository

import com.example.todoapp.data.model.Task
import com.example.todoapp.common.Result

interface TaskRepository {

    suspend fun addTask(title: String, body: String): Result<Unit>

    suspend fun getAllTasks(): Result<List<Task>>

    suspend fun deleteTask(taskId: String): Result<Unit>

    suspend fun updateTask(taskId: String, title: String, body: String): Result<Unit>

}