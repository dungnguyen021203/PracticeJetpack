package com.example.todoapp.ui.state

import com.example.todoapp.data.model.Task

// All the state in single screen
data class TaskScreenUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val errorMessage: String ?= null,
    val taskToBeUpdated: Task ?= null,
    val isShowAddTaskDialog: Boolean = false,
    val isShowUpdateTaskDialog: Boolean = false,
    val currentTextFieldTitle: String = "",
    val currentTextFieldBody: String = "",
)
