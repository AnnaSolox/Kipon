package com.annasolox.kipon.data.api.service

import com.annasolox.kipon.data.api.models.request.create.LoginRequest
import com.annasolox.kipon.data.api.models.request.create.UserCreate

interface AuthService {
    suspend fun login (loginRequest: LoginRequest): String
    suspend fun register(userCreate: UserCreate): Result<Unit>
}