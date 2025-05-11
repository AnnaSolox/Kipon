package com.annasolox.kipon.data.api.service

import com.annasolox.kipon.core.TokenProvider
import com.annasolox.kipon.data.api.models.request.create.LoginRequest
import com.annasolox.kipon.data.api.models.request.create.UserCreate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthServiceImpl(
    private val client: HttpClient,
    private val tokenProvider: TokenProvider
) :
    AuthService {

    override suspend fun login(loginRequest: LoginRequest): String {

        val response = client.post("auth/login") {
            setBody(loginRequest)
        }
        val token = response.body<String>()
        tokenProvider.token = token

        return token
    }

    override suspend fun register(userCreate: UserCreate) {
        client.post("auth/register") {
            setBody(userCreate)
        }
    }
}
