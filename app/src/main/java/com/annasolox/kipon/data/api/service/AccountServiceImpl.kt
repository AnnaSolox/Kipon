package com.annasolox.kipon.data.api.service

import com.annasolox.kipon.data.api.models.request.create.AccountCreate
import com.annasolox.kipon.data.api.models.request.create.SavingCreate
import com.annasolox.kipon.data.api.models.response.AccountResponse
import com.annasolox.kipon.data.api.models.response.SavingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AccountServiceImpl(private val client: HttpClient): AccountService {

    override suspend fun fecthAccounts(): List<AccountResponse> {
        return client.get("huchas").body()
    }

    override suspend fun fechtAccoutnById(id: Long): AccountResponse {
        return client.get("huchas/$id").body()
    }

    override suspend fun createNewAccount(accountCreate: AccountCreate): AccountResponse {
        return client.post("huchas"){
            setBody(accountCreate)
        }.body()

    }

    override suspend fun createNewContribution(
        accountId: Long,
        savingCreate: SavingCreate
    ): SavingResponse {
        return client.post("huchas/$accountId/ahorros") {
            setBody(savingCreate)
        }.body()
    }
}