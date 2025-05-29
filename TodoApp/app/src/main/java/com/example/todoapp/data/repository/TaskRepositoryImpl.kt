package com.example.todoapp.data.repository

import androidx.compose.material3.Tab
import com.example.todoapp.common.COLLECTION_PATH_NAME
import com.example.todoapp.common.PLEASE_CHECK_INTERNET_CONNECTION
import com.example.todoapp.common.getCurrentTimeAsString
import com.example.todoapp.data.model.Task
import com.example.todoapp.di.IoDispatcher
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import com.example.todoapp.common.Result
import com.example.todoapp.common.convertDateFormat
import kotlinx.coroutines.tasks.await

class TaskRepositoryImpl @Inject constructor(
    private val todoAppDb: FirebaseFirestore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TaskRepository {
    override suspend fun addTask(
        title: String,
        body: String
    ): Result<Unit> {
        return try {
            withContext(ioDispatcher) {
                val task = hashMapOf(
                    "title" to title,
                    "body" to body,
                    "createdAt" to getCurrentTimeAsString()
                )

                val addTaskTimeOut = withTimeoutOrNull(10000L) {
                    todoAppDb.collection(COLLECTION_PATH_NAME).add(task)
                }

                if (addTaskTimeOut == null) {
                    Result.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }

                Result.Success(Unit)

            }
        } catch (e: Exception) {
            Result.Failure(exception = e)
        }
    }

    override suspend fun getAllTasks(): Result<List<Task>> {
        return try {
            withContext(ioDispatcher) {
                val fetchingAllTasksTimeout = withTimeoutOrNull(10000L) {
                    todoAppDb.collection(COLLECTION_PATH_NAME)
                        .get()
                        .await()
                        .documents.map { document ->
                            Task(
                                taskId = document.id,
                                title = document.getString("title") ?: "",
                                body = document.getString("body") ?: "",
                                createdAt = convertDateFormat(
                                    dateString = document.getString("createdAt") ?: ""
                                )
                            )
                        }
                }

                if (fetchingAllTasksTimeout == null) {
                    Result.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }

                Result.Success(fetchingAllTasksTimeout?.toList() ?: emptyList())
            }

        } catch (e: Exception) {
            Result.Failure(exception = e)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            withContext(ioDispatcher) {
                val deleteTaskTimeout = withTimeoutOrNull(10000L) {
                    todoAppDb.collection(COLLECTION_PATH_NAME)
                        .document(taskId).delete()
                }

                if (deleteTaskTimeout == null) {
                    Result.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }

                Result.Success(Unit)
            }
        } catch (e: Exception) {
            Result.Failure(exception = e)
        }
    }

    override suspend fun updateTask(
        taskId: String,
        title: String,
        body: String
    ): Result<Unit> {
        return try {
            withContext(ioDispatcher) {
                val taskUpdate: Map<String, String> = hashMapOf(
                    "title" to title,
                    "body" to body
                )

                val updateTaskTimeout = withTimeoutOrNull(10000L) {
                    todoAppDb.collection(COLLECTION_PATH_NAME)
                        .document(taskId)
                        .update(taskUpdate)
                }

                if(updateTaskTimeout == null) {
                    Result.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }

                Result.Success(Unit)
            }
        } catch (e: Exception) {
            Result.Failure(exception = e)
        }
    }
}