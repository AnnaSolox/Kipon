package com.annasolox.kipon.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasolox.kipon.data.api.models.request.create.LoginRequest
import com.annasolox.kipon.data.repository.AuthRepository
import com.annasolox.kipon.ui.models.LoginUiState
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {
    var loginState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    fun login(userName: String, password: String){
        viewModelScope.launch {
            loginState = LoginUiState.Loading
            try{
                val token  = authRepository.login(LoginRequest(userName, password))
                loginState = LoginUiState.Success(token)
            } catch (e: Exception){
                loginState = LoginUiState.Error("Usuario o contraseña incorrectos")
            }
        }
    }

    fun resetState(){
        loginState = LoginUiState.Idle
    }
}