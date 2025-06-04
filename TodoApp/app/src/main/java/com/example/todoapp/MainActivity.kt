package com.example.todoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todoapp.common.SIDE_EFFECTS_KEY
import com.example.todoapp.ui.components.AddTaskDialogComponent
import com.example.todoapp.ui.events.TaskScreenUiEvents
import com.example.todoapp.ui.side_effects.TaskScreenSideEffects
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.ui.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.ui.components.LoadingComponent
import com.example.todoapp.ui.components.TaskCardComponent
import com.example.todoapp.ui.components.UpdateTaskDialogComponent
import com.example.todoapp.ui.components.WelcomeMessageComponent
import androidx.compose.foundation.lazy.items
import com.example.todoapp.ui.components.EmptyComponent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val tasksViewModel: TaskViewModel = viewModel()

            val uiState = tasksViewModel.state.collectAsState().value

            val effects = tasksViewModel.effect

            val snackBarHostState = remember { SnackbarHostState() }

            LaunchedEffect(key1 = SIDE_EFFECTS_KEY) {
                effects.onEach { effect ->
                    when (effect) {
                        is TaskScreenSideEffects.ShowSnackBarMessage -> {
                            snackBarHostState.showSnackbar(
                                message = effect.message,
                                duration = SnackbarDuration.Short,
                                actionLabel = "DISMISS"
                            )
                        }
                    }
                }.collect()
            }

            TodoAppTheme {
                if (uiState.isShowAddTaskDialog) {
                    AddTaskDialogComponent(
                        uiState = uiState,
                        setTaskTitle = { title ->
                            tasksViewModel.sendEvent(
                                event = TaskScreenUiEvents.OnChangeTaskTitle(
                                    title
                                )
                            )
                        },
                        setTaskBody = { body ->
                            tasksViewModel.sendEvent(
                                event = TaskScreenUiEvents.OnChangeTaskBody(body)
                            )
                        },
                        saveTask = {
                            tasksViewModel.sendEvent(
                                event = TaskScreenUiEvents.AddTask(
                                    title = uiState.currentTextFieldTitle,
                                    body = uiState.currentTextFieldBody
                                )
                            )
                        },
                        closeDialog = {
                            tasksViewModel.sendEvent(
                                event = TaskScreenUiEvents.OnChangeAddTaskDialogState(show = false)
                            )
                        }
                    )
                }

                if (uiState.isShowUpdateTaskDialog) {
                    UpdateTaskDialogComponent(
                        uiState = uiState,
                        setTaskTitle = { title ->
                            tasksViewModel.sendEvent(
                                event = TaskScreenUiEvents.OnChangeTaskTitle(
                                    title = title
                                )
                            )
                        },
                        setTaskBody = { body ->
                            tasksViewModel.sendEvent(
                                event = TaskScreenUiEvents.OnChangeTaskBody(
                                    body = body
                                )
                            )
                        },
                        saveTask = { tasksViewModel.sendEvent(event = TaskScreenUiEvents.UpdateTask) },
                        closeDialog = {
                            tasksViewModel.sendEvent(
                                event = TaskScreenUiEvents.OnChangeAddTaskDialogState(show = false)
                            )
                        },
                        task = uiState.taskToBeUpdated
                    )
                }

                Scaffold (
                    snackbarHost = {
                        SnackbarHost(snackBarHostState)
                    },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            icon = {
                                Icon(
                                    imageVector = Icons.Rounded.AddCircle,
                                    contentDescription = "Add Task",
                                    tint = Color.White
                                )
                            },
                            text = {
                                Text(text = "Add Task", color = Color.White)
                            },
                            onClick = {
                                tasksViewModel.sendEvent(
                                    event = TaskScreenUiEvents.OnChangeAddTaskDialogState(show = true)
                                )
                            },
                            modifier = Modifier.padding(horizontal = 12.dp),
                            containerColor = Color.Black,
                            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
                        )
                    },
                    containerColor = Color(0XFFFAFAFA)
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                        when {
                            uiState.isLoading -> {
                                LoadingComponent()
                            }

                            !(uiState.isLoading) && uiState.tasks.isNotEmpty() -> {
                                LazyColumn(contentPadding = PaddingValues(14.dp)) {
                                    item{
                                        WelcomeMessageComponent()
                                        Spacer(modifier = Modifier.height(30.dp))
                                    }

                                    items(uiState.tasks) { task->
                                        TaskCardComponent(
                                            deleteTask = { taskId ->
                                                tasksViewModel.sendEvent(event = TaskScreenUiEvents.DeleteTask(taskId = taskId))
                                            },
                                            updateTask = { task ->
                                                tasksViewModel.sendEvent(event = TaskScreenUiEvents.OnChangeUpdateDialogState(show = true))
                                                tasksViewModel.sendEvent(event = TaskScreenUiEvents.SetTaskToBeUpdated(taskToBeUpdated = task))
                                            },
                                            task = task
                                        )
                                    }
                                }
                            }

                            !(uiState.isLoading) && uiState.tasks.isEmpty() -> {
                                EmptyComponent()
                            }
                        }
                    }
                }
            }



        }
    }
}
