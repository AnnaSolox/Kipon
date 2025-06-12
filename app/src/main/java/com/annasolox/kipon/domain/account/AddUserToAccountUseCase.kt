package com.annasolox.kipon.domain.account

import com.annasolox.kipon.data.api.models.request.create.UserAccountCreate
import com.annasolox.kipon.data.repository.AccountRepository

class AddUserToAccountUseCase(
    private val accountRepository: AccountRepository
) {

    data class Result(
        val success: Boolean,
        val errorMessage: String? = null
    )

    suspend operator fun invoke (userId: Long, accountId: Long): Result {
        return try {
            val userAccountCreate = UserAccountCreate(
                userId = userId,
                accountId = accountId,
                role = "Miembro"
            )

            accountRepository.addUserToAccount(userAccountCreate)
            Result(success = true)
        } catch (e: Exception) {
            Result(success = false, errorMessage = e.message)
        }
    }
}