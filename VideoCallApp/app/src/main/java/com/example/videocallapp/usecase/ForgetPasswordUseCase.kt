package com.example.videocallapp.usecase

import com.example.videocallapp.common.Resource
import com.example.videocallapp.helper.await
import com.example.videocallapp.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ForgetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseFirestore: FirebaseFirestore
) {
    suspend operator fun invoke(email: String): Resource<Unit> {
        val userSnapshot = firebaseFirestore.collection("users").whereEqualTo("email", email).get().await()
        if (userSnapshot.isEmpty) {
            Resource.Failure(Exception("Email không được đăng kí"))
        }
        if (email.isBlank()) {
            Resource.Failure(Exception("Không được để trống email"))
        }
        return authRepository.resetPassword(email)
    }
}