package com.example.practingandroidstudio.di

import android.content.Context
import androidx.room.Room
import com.example.practingandroidstudio.data.PersonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder (
        context,
        PersonDatabase::class.java,
        "my_database"
    ).createFromAsset("database/person.db").build()

    @Singleton
    @Provides
    fun provideDao(database: PersonDatabase) = database.PersonDao()
}