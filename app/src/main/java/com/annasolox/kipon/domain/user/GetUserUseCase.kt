package com.annasolox.kipon.domain.user

import android.content.SharedPreferences
import com.annasolox.kipon.core.utils.mappers.UserMapper.toUserHomeScreen
import com.annasolox.kipon.core.utils.mappers.UserMapper.toUserProfileScreenFromUserResponse
import com.annasolox.kipon.data.repository.UserRepository
import com.annasolox.kipon.ui.models.UserHomeScreen
import com.annasolox.kipon.ui.models.UserProfileScreen


data class GetUserResult(
    val userHome: UserHomeScreen,
    val userProfile: UserProfileScreen
)

class GetUserUseCase (
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences) {

    suspend operator fun invoke(): Result<GetUserResult> {
        return try {
            val username = sharedPreferences.getString("username", null)
                ?: return Result.failure(Exception("No username stored"))

            val response = userRepository.getUserByUsername(username)
            val home = toUserHomeScreen(response)
            val profile = toUserProfileScreenFromUserResponse(response)
            Result.success(GetUserResult(home, profile))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}