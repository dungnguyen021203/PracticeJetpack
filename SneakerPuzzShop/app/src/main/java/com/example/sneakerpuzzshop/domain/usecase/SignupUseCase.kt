package com.example.sneakerpuzzshop.domain.usecase;

import com.example.sneakerpuzzshop.common.Resource
import com.example.sneakerpuzzshop.domain.model.UserModel
import com.example.sneakerpuzzshop.domain.repository.AuthRepository
import com.example.sneakerpuzzshop.utils.await
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject;

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore,
) {
    suspend operator fun invoke (email: String, name: String, password: String): Resource<FirebaseUser> {
        if (name.isBlank() || email.isBlank() || password.isBlank())
            return Resource.Failure(Exception("Không để trống các mục"))

        val userSnapshot = firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()

        if (!userSnapshot.isEmpty) {
            return Resource.Failure(Exception("Email đã được đăng ký"))
        }

        val result =  authRepository.signUp(email, name, password)
        if (result is Resource.Success) {
            val user = result.data
            val userModel = UserModel(uid = user.uid, name = name, email = email)
            firestore.collection("users").document(user.uid).set(userModel).await()
        }
        return result
    }
}
