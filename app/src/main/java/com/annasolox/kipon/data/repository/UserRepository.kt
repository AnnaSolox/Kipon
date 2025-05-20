package com.annasolox.kipon.data.repository

import android.content.SharedPreferences
import com.annasolox.kipon.core.utils.mappers.UserMapper
import com.annasolox.kipon.data.api.models.request.patch.UserPatch
import com.annasolox.kipon.data.api.models.response.UserResponse
import com.annasolox.kipon.data.api.service.UserService
import com.annasolox.kipon.ui.models.SearchedUser

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

    suspend fun updateCurrentUser(userPatch: UserPatch): UserResponse {
        val userId = getCurrentUserId()
        if (userId == -1L) throw Exception("ID de usuario no disponible o sesión no iniciada")
        return userService.updateUserInformation(userId, userPatch)
    }

    suspend fun fetchUsersByPartialName(partialName: String): List<SearchedUser>{
        val response = userService.fetchUserByPartialUsername(partialName)
        val searchedUserLists = response.map { UserMapper.toSearchedUser(it) }
        return searchedUserLists
    }
}