package com.annasolox.kipon.ui.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasolox.kipon.data.api.models.request.create.LoginRequest
import com.annasolox.kipon.data.api.utils.mappers.UserMapper
import com.annasolox.kipon.data.repository.AuthRepository
import com.annasolox.kipon.ui.models.LoginUiState
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    var userName by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordHidden by mutableStateOf(true)
    var loginState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            loginState = LoginUiState.Loading
            try {
                val token = authRepository.login(LoginRequest(userName, password))
                saveToken(token)
                loginState = LoginUiState.Success(token)
                Log.d("AuthViewModel", "Login successfull!! Token: $token")
            } catch (e: Exception) {
                loginState = LoginUiState.Error("Usuario o contraseña incorrectos")
                Log.d("AuthViewModel", "Usuario o contraseña incorrectos")
            }
        }
    }

    fun register(
        userName: String,
        password: String,
        email: String,
        completeName: String,
        phone: String,
        address: String,
        photo: String? = null
    ) {
        viewModelScope.launch {
            try {
                authRepository.register(
                    UserMapper.toUserCreate(
                        userName,
                        password,
                        email,
                        completeName,
                        phone,
                        address,
                        photo
                    )
                )
                loginState = LoginUiState.Success("")

            } catch (e: Exception) {
                loginState = LoginUiState.Error("Error al registrar el usuario")
                Log.d("AuthViewModel", "Error al registrar el usuario: ${e.message}")
            }
        }
    }

    private fun saveToken(token: String) {
        sharedPreferences.edit {
            putString("auth_token", token)
        }
    }

    fun resetState() {
        loginState = LoginUiState.Idle
    }
}