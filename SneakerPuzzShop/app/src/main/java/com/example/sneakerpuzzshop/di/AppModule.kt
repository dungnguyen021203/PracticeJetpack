package com.example.sneakerpuzzshop.di

import android.content.Context
import com.example.sneakerpuzzshop.data.repository.AuthRepositoryImpl
import com.example.sneakerpuzzshop.domain.repository.AuthRepository
import com.example.sneakerpuzzshop.domain.usecase.GoogleLoginUseCase
import com.example.sneakerpuzzshop.domain.usecase.LoginUseCase
import com.example.sneakerpuzzshop.domain.usecase.SignupUseCase
import com.example.sneakerpuzzshop.utils.GoogleSignInHelper
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
    fun provideFirebaseFirestore() : FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideGoogleSignInHelper(
        @ApplicationContext context: Context,
    ): GoogleSignInHelper {
        return GoogleSignInHelper(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(authRepository: AuthRepository) : LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignupUseCase(authRepository: AuthRepository, firestore: FirebaseFirestore) : SignupUseCase {
        return SignupUseCase(authRepository, firestore)
    }

    @Provides
    @Singleton
    fun provideGoogleLoginUseCase(authRepository: AuthRepository, firestore: FirebaseFirestore) : GoogleLoginUseCase {
        return GoogleLoginUseCase(authRepository, firestore)
    }
}