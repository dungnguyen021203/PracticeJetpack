package com.example.todoapp.ui.side_effects

sealed class TaskScreenSideEffects {
    data class ShowSnackBarMessage(val message: String): TaskScreenSideEffects()
}