package com.annasolox.kipon.data.api.service

import com.annasolox.kipon.data.api.models.request.create.AccountCreate
import com.annasolox.kipon.data.api.models.request.create.SavingCreate
import com.annasolox.kipon.data.api.models.response.AccountResponse
import com.annasolox.kipon.data.api.models.response.SavingResponse

interface AccountService {
    suspend fun fecthAccounts(): List<AccountResponse>
    suspend fun fechtAccoutnById(id: Long): AccountResponse
    suspend fun createNewAccount(accountCreate: AccountCreate): AccountResponse
    suspend fun createNewContribution(accountId: Long, savingCreate: SavingCreate): SavingResponse
}