package com.annasolox.kipon.data.repository

import com.annasolox.kipon.data.api.models.response.AccountResponse
import com.annasolox.kipon.data.api.service.AccountService

class AccountRepository(private val accountService: AccountService) {
    suspend fun getAccounts(): List<AccountResponse> {
        return accountService.fecthAccounts()
    }

    suspend fun getAccountById(id: Long): AccountResponse {
        return accountService.fechtAccoutnById(id)
    }
}