package com.annasolox.kipon.domain.user

import com.annasolox.kipon.data.repository.UserRepository
import com.annasolox.kipon.ui.models.SearchedUser

class SearchUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(partialName: String): List<SearchedUser> {
        return userRepository.fetchUsersByPartialName(partialName)
    }
}