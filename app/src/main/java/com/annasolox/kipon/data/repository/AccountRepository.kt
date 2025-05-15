package com.annasolox.kipon.data.repository

import com.annasolox.kipon.core.utils.mappers.AccountMapper.toAccountOverview
import com.annasolox.kipon.core.utils.mappers.AccountMapper.toSavingUi
import com.annasolox.kipon.data.api.models.request.create.AccountCreate
import com.annasolox.kipon.data.api.models.request.create.SavingCreate
import com.annasolox.kipon.data.api.models.response.AccountResponse
import com.annasolox.kipon.data.api.service.AccountService
import com.annasolox.kipon.ui.models.AccountOverview
import com.annasolox.kipon.ui.models.Saving

class AccountRepository(
    private val accountService: AccountService,
    private val userRepository: UserRepository
) {
    suspend fun getAccounts(): List<AccountResponse> {
        return accountService.fecthAccounts()
    }

    suspend fun getAccountById(id: Long): AccountResponse {
        return accountService.fechtAccoutnById(id)
    }

    suspend fun accountCreate(accountCreate: AccountCreate): AccountOverview {
        val currentUserId = userRepository.getCurrentUserId()
        val createdAccount = accountService.createNewAccount(accountCreate)
        createdAccount.savings?.let {
            val savingsList = createdAccount.savings ?: arrayListOf()
            createdAccount.savings = savingsList
        }

        return toAccountOverview(createdAccount)
    }

    suspend fun createNewContribution(
        accountId: Long,
        savingCreate: SavingCreate
    ): Saving {
        val savingResponse = accountService.createNewContribution(accountId, savingCreate)
        return toSavingUi(savingResponse)
    }
}