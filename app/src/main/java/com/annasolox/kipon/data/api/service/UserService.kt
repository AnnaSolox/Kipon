package com.annasolox.kipon.data.api.service

import com.annasolox.kipon.data.api.models.request.patch.UserPatch
import com.annasolox.kipon.data.api.models.response.UserResponse

interface UserService {
    suspend fun fetchUserById(id: Long): UserResponse
    suspend fun fetchUserByUsername(username: String): UserResponse
    suspend fun updateUserInformation(id: Long, userPatch: UserPatch): UserResponse
}