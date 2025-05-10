package com.annasolox.kipon.data.api.service

import com.annasolox.kipon.core.network.KtorClientProvider
import com.annasolox.kipon.data.api.models.request.create.LoginRequest
import com.annasolox.kipon.data.api.models.request.create.UserCreate
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthServiceImpl : AuthService {

    private val client = KtorClientProvider.httpClient

    override suspend fun login(loginRequest: LoginRequest): String {
        val response = client.post("/auth/login") {
            setBody(loginRequest)
        }
        val token = response.body<String>()
        KtorClientProvider.authToken = token

        return token
    }

    override suspend fun register(userCreate: UserCreate) {
        client.post("/auth/register") {
            setBody(userCreate)
        }
    }
}
