package com.annasolox.kipon.data.repository

import com.annasolox.kipon.data.api.models.response.UserResponse
import com.annasolox.kipon.data.api.service.UserService

class UserRepository(private val userService: UserService) {
    suspend fun getUserById(id: Long): UserResponse {
        return userService.fetchUserById(id);
    };

    suspend fun getUserByUsername(username: String): UserResponse {
        return userService.fetchUserByUsername(username);
    };
}