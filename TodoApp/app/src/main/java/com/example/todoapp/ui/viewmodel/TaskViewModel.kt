package com.example.todoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.common.Result
import com.example.todoapp.data.model.Task
import com.example.todoapp.data.repository.TaskRepository
import com.example.todoapp.ui.events.TaskScreenUiEvents
import com.example.todoapp.ui.side_effects.TaskScreenSideEffects
import com.example.todoapp.ui.state.TaskScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _state: MutableStateFlow<TaskScreenUiState> = MutableStateFlow(TaskScreenUiState())
    val state: StateFlow<TaskScreenUiState> = _state.asStateFlow()

    private val _effect: Channel<TaskScreenSideEffects> = Channel()
    val effect = _effect.receiveAsFlow()

    // UI -> viewmodel
    fun sendEvent(event: TaskScreenUiEvents) {
        reduce(event = event, oldState = state.value)
    }

    private fun setState(newState: TaskScreenUiState) {
        _state.value = newState
    }

    private fun setEffect(builder: () -> TaskScreenSideEffects) {
        val effectValue = builder()
        viewModelScope.launch {
            _effect.send(effectValue)
        }
    }

    private fun reduce(event: TaskScreenUiEvents, oldState: TaskScreenUiState) {
        when (event) {
            is TaskScreenUiEvents.AddTask -> {
                addTask(oldState = oldState, title = event.title, body = event.body)
            }

            is TaskScreenUiEvents.DeleteTask -> {
                deleteTask(oldState = oldState, taskId = event.taskId)
            }

            TaskScreenUiEvents.GetTasks -> {
                getAllTasks(oldState = oldState)
            }

            is TaskScreenUiEvents.OnChangeAddTaskDialogState -> {
                onChangeAddTaskDialogState(oldState = oldState, isShown = event.show)
            }

            is TaskScreenUiEvents.OnChangeTaskBody -> {
                onChangeTaskBody(oldState = oldState, body = event.body)
            }

            is TaskScreenUiEvents.OnChangeTaskTitle -> {
                onChangeTaskTitle(oldState = oldState, title = event.title)
            }

            is TaskScreenUiEvents.OnChangeUpdateDialogState -> {
                onChangeUpdateTaskDialogState(oldState = oldState, isShown = event.show)
            }

            is TaskScreenUiEvents.SetTaskToBeUpdated -> {
                setTaskToBeUpdated(oldState = oldState, task = event.taskToBeUpdated)
            }

            TaskScreenUiEvents.UpdateTask -> {
                updateTask(oldState = oldState)
            }
        }
    }

    private fun updateTask(oldState: TaskScreenUiState) {
        viewModelScope.launch {
            setState(
                oldState.copy(
                    isLoading = true
                )
            )

            val title = oldState.currentTextFieldTitle
            val body = oldState.currentTextFieldBody
            val taskToBeUpdated = oldState.taskToBeUpdated

            when (val result = repository.updateTask(
                title = title,
                body = body,
                taskId = taskToBeUpdated?.taskId ?: ""
            )) {
                is Result.Failure -> {
                    setState(
                        oldState.copy(
                            isLoading = false
                        )
                    )

                    val errorMessage =
                        result.exception.message ?: "An error occurred when updating this"

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Result.Success -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            currentTextFieldTitle = "",
                            currentTextFieldBody = ""
                        )
                    )

                    sendEvent(TaskScreenUiEvents.OnChangeUpdateDialogState(show = false))

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = "Update successfully") }

                    sendEvent(TaskScreenUiEvents.GetTasks)
                }
            }
        }
    }

    private fun setTaskToBeUpdated(
        oldState: TaskScreenUiState,
        task: Task
    ) {
        setState(oldState.copy(taskToBeUpdated = task))
    }

    private fun onChangeUpdateTaskDialogState(
        oldState: TaskScreenUiState,
        isShown: Boolean
    ) {
        setState(
            oldState.copy(
                isShowUpdateTaskDialog = isShown
            )
        )
    }

    private fun onChangeTaskTitle(
        oldState: TaskScreenUiState,
        title: String
    ) {
        setState(oldState.copy(currentTextFieldTitle = title))
    }

    private fun onChangeTaskBody(
        oldState: TaskScreenUiState,
        body: String
    ) {
        setState(oldState.copy(currentTextFieldBody = body))
    }

    private fun onChangeAddTaskDialogState(
        oldState: TaskScreenUiState,
        isShown: Boolean
    ) {
        setState(
            oldState.copy(
                isShowAddTaskDialog = isShown
            )
        )
    }

    private fun getAllTasks(oldState: TaskScreenUiState) {
        viewModelScope.launch {
            setState(
                oldState.copy(
                    isLoading = true
                )
            )

            when (val result = repository.getAllTasks()) {
                is Result.Failure -> {
                    setState(
                        oldState.copy(
                            isLoading = false
                        )
                    )

                    val errorMessage =
                        result.exception.message ?: "An error occurred when fetching this"

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Result.Success -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            tasks = result.data
                        )
                    )
                }
            }
        }
    }

    private fun deleteTask(
        oldState: TaskScreenUiState,
        taskId: String
    ) {
        viewModelScope.launch {
            setState(
                oldState.copy(
                    isLoading = true
                )
            )

            when (val result = repository.deleteTask(taskId = taskId)) {
                is Result.Failure -> {
                    setState(
                        oldState.copy(
                            isLoading = false
                        )
                    )

                    val errorMessage =
                        result.exception.message ?: "An error occurred when delete this"

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Result.Success -> {
                    setState(
                        oldState.copy(
                            isLoading = false
                        )
                    )
                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = "Task deleted successfully") }
                    sendEvent(TaskScreenUiEvents.GetTasks)
                }
            }
        }
    }

    private fun addTask(
        oldState: TaskScreenUiState,
        title: String,
        body: String
    ) {
        viewModelScope.launch {
            setState(
                oldState.copy(
                    isLoading = true
                )
            )

            when (val result = repository.addTask(title = title, body = body)) {
                is Result.Failure -> {
                    setState(
                        oldState.copy(
                            isLoading = true
                        )
                    )

                    val errorMessage =
                        result.exception.message ?: "An error occurred when adding this"

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Result.Success -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            currentTextFieldTitle = "",
                            currentTextFieldBody = ""
                        )
                    )

                    sendEvent(event = TaskScreenUiEvents.OnChangeAddTaskDialogState(show = false))

                    sendEvent(TaskScreenUiEvents.GetTasks)

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = "Task added successfully") }
                }
            }
        }
    }
}