package com.annasolox.kipon.data.repository

import android.content.SharedPreferences
import com.annasolox.kipon.data.api.models.response.UserResponse
import com.annasolox.kipon.data.api.service.UserService

class UserRepository(
    private val userService: UserService,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun getUserById(id: Long): UserResponse {
        return userService.fetchUserById(id)
    }

    suspend fun getUserByUsername(username: String): UserResponse {
        return userService.fetchUserByUsername(username)
    }

    suspend fun getCurrentUser(): UserResponse? {
        val username = sharedPreferences.getString("username", null) ?: return null
        return getUserByUsername(username)
    }

    suspend fun getCurrentUserId(): Long{
        return getCurrentUser()?.id ?: -1
    }
}