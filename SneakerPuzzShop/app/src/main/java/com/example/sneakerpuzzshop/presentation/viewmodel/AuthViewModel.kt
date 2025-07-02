package com.example.sneakerpuzzshop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sneakerpuzzshop.common.Resource
import com.example.sneakerpuzzshop.domain.repository.AuthRepository
import com.example.sneakerpuzzshop.domain.usecase.LoginUseCase
import com.example.sneakerpuzzshop.domain.usecase.SignupUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signUpFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<Resource<FirebaseUser>?> = _signUpFlow

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        if (authRepository.currentUser != null) {
            _loginFlow.value = Resource.Success(authRepository.currentUser!!)
        }
    }

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

    fun signOut() {
        authRepository.logOut()
        _loginFlow.value = null
        _signUpFlow.value = null
    }

}