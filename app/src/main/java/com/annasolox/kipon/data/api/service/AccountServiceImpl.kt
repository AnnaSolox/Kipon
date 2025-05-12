package com.annasolox.kipon.data.api.service

import com.annasolox.kipon.data.api.models.response.AccountResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class AccountServiceImpl(private val client: HttpClient): AccountService {

    override suspend fun fecthAccounts(): List<AccountResponse> {
        return client.get("huchas").body()
    }

    override suspend fun fechtAccoutnById(id: Long): AccountResponse {
        return client.get("huchas/{id}") {
            parameter("id", id)
        }.body()
    }
}