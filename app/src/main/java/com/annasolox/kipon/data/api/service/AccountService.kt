package com.annasolox.kipon.data.api.service

import com.annasolox.kipon.data.api.models.request.create.AccountCreate
import com.annasolox.kipon.data.api.models.response.AccountResponse

interface AccountService {
    suspend fun fecthAccounts(): List<AccountResponse>
    suspend fun fechtAccoutnById(id: Long): AccountResponse
    suspend fun createNewAccount(accountCreate: AccountCreate): AccountResponse
}