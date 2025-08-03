package com.example.videocallapp.repository

import com.example.videocallapp.common.Resource
import com.example.videocallapp.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun login(email: String, password: String): Resource<FirebaseUser>

    suspend fun signUp(email: String, name: String, password: String): Resource<FirebaseUser>

    suspend fun loginWithGoogle(idToken: String): Resource<FirebaseUser>

    fun logOut()

    suspend fun resetPassword(email: String): Resource<Unit>

    suspend fun getUserInformation(): Resource<UserModel>

    suspend fun changePassword(oldPassword: String, newPassword: String): Resource<Unit>

    suspend fun updateUser(uid: String, updatedUser: UserModel): Resource<Unit>

    suspend fun deleteAccount(uid: String): Resource<Unit>
}