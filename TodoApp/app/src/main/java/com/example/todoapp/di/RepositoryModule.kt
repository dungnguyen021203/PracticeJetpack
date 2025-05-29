package com.example.todoapp.di

import com.example.todoapp.data.repository.TaskRepository
import com.example.todoapp.data.repository.TaskRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(
        firebaseFirestore: FirebaseFirestore,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): TaskRepository {
        return TaskRepositoryImpl(
            todoAppDb = firebaseFirestore,
            ioDispatcher = ioDispatcher,
        )
    }
}