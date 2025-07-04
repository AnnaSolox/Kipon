package com.annasolox.kipon.data.api.service

import com.annasolox.kipon.data.api.models.request.create.AccountCreate
import com.annasolox.kipon.data.api.models.request.create.SavingCreate
import com.annasolox.kipon.data.api.models.request.create.UserAccountCreate
import com.annasolox.kipon.data.api.models.request.patch.AccountPatch
import com.annasolox.kipon.data.api.models.response.AccountResponse
import com.annasolox.kipon.data.api.models.response.SavingResponse
import com.annasolox.kipon.data.api.models.response.UserResponse

interface AccountService {
    suspend fun fecthAccounts(): List<AccountResponse>
    suspend fun fechtAccoutnById(id: Long): AccountResponse
    suspend fun createNewAccount(accountCreate: AccountCreate): AccountResponse
    suspend fun createNewContribution(accountId: Long, savingCreate: SavingCreate): SavingResponse
    suspend fun updateAccountInformation(id: Long, accountPatch: AccountPatch): AccountResponse
    suspend fun addUserToAccount(userAccountCreate: UserAccountCreate): UserResponse
}