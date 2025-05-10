package com.annasolox.kipon.ui.models

sealed class LoginUiState {
    object Idle: LoginUiState()
    object Loading: LoginUiState()
    data class Success(val token: String): LoginUiState()
    data class Error(val message: String): LoginUiState()
}