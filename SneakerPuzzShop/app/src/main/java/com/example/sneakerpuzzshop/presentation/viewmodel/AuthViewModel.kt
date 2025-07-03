package com.example.sneakerpuzzshop.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sneakerpuzzshop.common.Resource
import com.example.sneakerpuzzshop.domain.repository.AuthRepository
import com.example.sneakerpuzzshop.domain.usecase.GoogleLoginUseCase
import com.example.sneakerpuzzshop.domain.usecase.LoginUseCase
import com.example.sneakerpuzzshop.domain.usecase.SignupUseCase
import com.example.sneakerpuzzshop.utils.GoogleSignInHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val googleSignInHelper: GoogleSignInHelper,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow.asStateFlow()

    private val _signUpFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<Resource<FirebaseUser>?> = _signUpFlow.asStateFlow()

    private val _googleLoginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val googleLoginFlow: StateFlow<Resource<FirebaseUser>?> = _googleLoginFlow.asStateFlow()

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = loginUseCase(email, password)
        _loginFlow.value = result
    }

    fun signUp(email: String, name: String, password: String) = viewModelScope.launch {
        _signUpFlow.value = Resource.Loading
        val result = signupUseCase(email, name, password)
        _signUpFlow.value = result
    }

    fun loginWithGoogle() = viewModelScope.launch {
        _googleLoginFlow.value = Resource.Loading

        when (val result = googleSignInHelper.getIdTokenFromGoogle()) {
            is Resource.Success -> {
                val idToken = result.data

                if (idToken.isEmpty()) {
                    _googleLoginFlow.value = Resource.Failure(Exception("idToken null hoặc rỗng"))
                    return@launch
                }

                val firebaseResult = googleLoginUseCase(idToken)
                _googleLoginFlow.value = firebaseResult
            }

            is Resource.Failure -> {
                _googleLoginFlow.value = result
            }

            is Resource.Loading -> {}
        }
    }

    fun signOut() {
        authRepository.logOut()
        _loginFlow.value = null
        _signUpFlow.value = null
        _googleLoginFlow.value = null

        viewModelScope.launch {
            googleSignInHelper.clearGoogleCredentialState()
        }
    }
}