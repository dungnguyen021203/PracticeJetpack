package com.example.sneakerpuzzshop.domain.usecase;

import com.example.sneakerpuzzshop.common.Resource
import com.example.sneakerpuzzshop.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject;

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke (email: String, name: String, password: String): Resource<FirebaseUser> {
        if (name.isBlank() || email.isBlank() || password.isBlank())
            return Resource.Failure(Exception("Không để trống các mục"))
        return authRepository.signUp(email, name, password)
    }
}
