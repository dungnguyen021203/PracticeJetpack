package com.example.sneakerpuzzshop.domain.usecase;

import com.example.sneakerpuzzshop.common.Resource
import com.example.sneakerpuzzshop.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class ForgetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(email: String): Resource<Unit> {
        if (email.isBlank())
            return Resource.Failure(Exception("Không được để trống email"))
        return authRepository.resetPassword(email)
    }
}
