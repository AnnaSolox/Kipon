package com.annasolox.kipon.domain.account

import com.annasolox.kipon.data.api.models.request.create.SavingCreate
import com.annasolox.kipon.data.repository.AccountRepository
import com.annasolox.kipon.data.repository.UserRepository
import com.annasolox.kipon.ui.models.Saving
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CreateContributionUseCase (
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository,
    private val mutex: Mutex
) {

    data class Result(
        val success: Boolean,
        val amountError: String? = null,
        val generalError: String? = null,
        val newContribution: Saving? = null
    )

    suspend operator fun invoke (accountId: Long, amount: Double?): Result {
        if (amount == null) {
            return Result(success = false, amountError = "Campo obligatorio")
        } else if (amount <= 0) {
            return Result(success = false, amountError = "La cantidad debe ser mayor a 0")
        }

        return try {
            mutex.withLock {
                val userId = userRepository.getCurrentUserId()
                val savingCreate = SavingCreate(userId, amount)
                val contribution = accountRepository.createNewContribution(accountId, savingCreate)
                Result(success = true, newContribution = contribution)
            }
        } catch (e: Exception) {
            Result(success = false, generalError = "Error al crear la contribución: ${e.message}")
        }
    }
}