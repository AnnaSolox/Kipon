package com.annasolox.kipon.data.repository

import com.annasolox.kipon.data.api.models.request.create.LoginRequest
import com.annasolox.kipon.data.api.models.request.create.UserCreate
import com.annasolox.kipon.data.api.service.AuthService

class AuthRepository(private val authService: AuthService) {
    suspend fun login (loginRequest: LoginRequest): String {
        return authService.login(loginRequest)
    }

    suspend fun register(user: UserCreate) = authService.register(user)
}