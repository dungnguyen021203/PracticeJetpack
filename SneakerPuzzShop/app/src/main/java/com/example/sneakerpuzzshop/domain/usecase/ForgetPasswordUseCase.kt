package com.example.sneakerpuzzshop.domain.usecase;

import com.example.sneakerpuzzshop.common.Resource
import com.example.sneakerpuzzshop.domain.repository.AuthRepository
import com.example.sneakerpuzzshop.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ForgetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore
){
    suspend operator fun invoke(email: String): Resource<Unit> {
        val userSnapshot = firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()

        if (userSnapshot.isEmpty) {
            return Resource.Failure(Exception("Email không được đăng kí"))
        }

        if (email.isBlank())
            return Resource.Failure(Exception("Không được để trống email"))
        return authRepository.resetPassword(email)
    }
}
