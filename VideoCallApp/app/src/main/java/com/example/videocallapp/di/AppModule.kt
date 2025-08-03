package com.example.videocallapp.di

import android.content.Context
import com.example.videocallapp.helper.GoogleSignInHelper
import com.example.videocallapp.repository.AuthRepository
import com.example.videocallapp.repository_impl.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideGoogleSignInHelper(
        @ApplicationContext context: Context
    ): GoogleSignInHelper {
        return GoogleSignInHelper(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, firebaseFirestore)
    }
}