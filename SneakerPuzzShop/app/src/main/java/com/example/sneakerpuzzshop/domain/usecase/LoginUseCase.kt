package com.example.sneakerpuzzshop.domain.usecase

import com.example.sneakerpuzzshop.common.Resource
import com.example.sneakerpuzzshop.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Resource<FirebaseUser> {
        if (email.isBlank() || password.isBlank())
            return Resource.Failure(Exception("Không được để trống email hoặc password"))
        return authRepository.login(email, password)
    }
}