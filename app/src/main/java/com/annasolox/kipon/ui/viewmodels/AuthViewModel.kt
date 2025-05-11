package com.annasolox.kipon.ui.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private var _loginState = MutableLiveData<LoginUiState>(LoginUiState.Idle)
    val loginState: LiveData<LoginUiState> get() = _loginState


    fun login(userName: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            try {
                val result = authRepository.login(LoginRequest(userName, password))
                val token = result.getOrThrow()

                if(token.isNotEmpty()){
                    _loginState.value = LoginUiState.Success(token)
                    saveToken(token)
                    Log.d("AuthViewModel", "Login successfull!! Token: $token")
                } else {
                    _loginState.value = LoginUiState.Error("Error al iniciar sesión")
                    Log.d("AuthViewModel", "Error al iniciar sesión")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error al iniciar sesión: ${e.message}")
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
                _loginState.value = LoginUiState.Success("")

            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error("Error al registrar el usuario")
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
        _loginState.value = LoginUiState.Idle
    }
}