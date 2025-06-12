package com.annasolox.kipon.domain.account

import com.annasolox.kipon.data.api.models.request.patch.AccountPatch
import com.annasolox.kipon.data.repository.AccountRepository
import java.time.LocalDate

class UpdateAccountUseCase (
    private val accountRepository: AccountRepository
) {
    data class Result(
        val success: Boolean,
        val nameError: String? = null,
        val moneyGoalError: String? = null,
        val generalError: String? = null
    )

    suspend operator fun invoke (
        accountId: Long,
        name: String?,
        moneyGoal: Double?,
        dateGoal: LocalDate?,
        photo: String?
    ): Result {
        var isValid = true
        var nameErr: String? = null
        var moneyGoalErr: String? = null

        if(!name.isNullOrBlank() && name.length > 100) {
            nameErr = "Debe tener menos de 100 caracteres"
            isValid = false
        }

        if (moneyGoal != null && moneyGoal < 0){
            moneyGoalErr = "La cantidad debe ser mayor a 0"
            isValid = false
        }

        if (!isValid) {
            return Result(
                success = false,
                nameError = nameErr,
                moneyGoalError = moneyGoalErr
            )
        }

        return try {
            val patch = AccountPatch(
                name = name,
                moneyGoal = moneyGoal,
                dateGoal = dateGoal,
                photo = photo,
                adminId = null
            )

            accountRepository.updateCurrentAccount(accountId, patch)
            Result(success = true)
        } catch (e: Exception) {
            Result(success = false, generalError = e.message)
        }
    }
}