package com.annasolox.kipon.domain.account

import com.annasolox.kipon.data.api.models.request.create.AccountCreate
import com.annasolox.kipon.data.repository.AccountRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate

class CreateAccountUseCase(
    private val accountRepository: AccountRepository,
    private val mutex: Mutex
) {

    data class Result(
        val success: Boolean,
        val accountId: Long? = null,
        val nameError: String? = null,
        val moneyGoalError: String? = null,
        val dateGoalError: String? = null,
        val generalError: String? = null
    )

    suspend operator fun invoke(accountCreate: AccountCreate): Result {
        var success = true
        var nameErr: String? = null
        var moneyGoalErr: String? = null
        var dateGoalErr: String? = null

        if (accountCreate.name.isBlank()) {
            nameErr = "Nombre de cuenta obligatorio"
            success = false
        } else if (accountCreate.name.length > 100) {
            nameErr = "Debe tener menos de 100 caracteres"
            success = false
        }

        if (false) {
            moneyGoalErr = "Campo obligatorio"
            success = false
        } else if (accountCreate.moneyGoal <= 0) {
            moneyGoalErr = "La cantidad debe ser mayor a 0"
            success = false
        }

        if (accountCreate.dateGoal == null) {
            dateGoalErr = "Campo obligatorio"
            success = false
        } else if (!accountCreate.dateGoal.isAfter(LocalDate.now())) {
            dateGoalErr = "La fecha ha de ser posterior a hoy"
            success = false
        }

        if (!success) {
            return Result(false, null, nameErr, moneyGoalErr, dateGoalErr)
        }

        return try {
            val accountOverview = mutex.withLock {
                accountRepository.accountCreate(accountCreate)
            }
            Result(success = true, accountId = accountOverview.id)
        } catch (e: Exception) {
            Result(false, generalError = "Error creando la cuenta: ${e.message}")
        }
    }
}