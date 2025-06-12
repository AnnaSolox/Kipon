package com.annasolox.kipon.domain.auth

import android.util.Log
import com.annasolox.kipon.data.api.models.request.create.LoginRequest
import com.annasolox.kipon.data.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    data class Result(
        val success: Boolean,
        val token: String? = null,
        val usernameError: String? = null,
        val passwordError: String? = null
    )

    suspend operator fun invoke(username: String, password: String): Result {
        var success = true
        var usernameErr: String? = null
        var passwordErr: String? = null

        if (username.isBlank()) {
            usernameErr = "Nombre de usuario obligatorio"
            success = false
        } else if (username.length > 50) {
            usernameErr = "Debe tener menos de 50 caracteres"
            success = false
        }

        if (password.isBlank()) {
            passwordErr = "Contraseña obligatoria"
            success = false
        } else if (password.length < 8) {
            passwordErr = "Debe tener al menos 8 caracteres"
            success = false
        } else if (password.length > 50) {
            passwordErr = "Debe tener menos de 50 caracteres"
            success = false
        }

        if (!success) {
            return Result(
                success = false,
                usernameError = usernameErr,
                passwordError = passwordErr
            )
        }

        return try {
            val response = authRepository.login(LoginRequest(username, password))
            val token = response.getOrThrow()

            if (token == "Nombre de usuario o contraseña incorrectos" || token.isEmpty()) {
                Result(success = false)
            } else {
                Result(success = true, token = token)
            }
        } catch (e: Exception) {
            Log.e("LoginUseCase", "${e.message}")
            Result(success = false)
        }
    }
}