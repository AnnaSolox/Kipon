package com.annasolox.kipon.data.api.service

import com.annasolox.kipon.data.api.models.response.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class UserServiceImpl(private val client: HttpClient): UserService {

    override suspend fun fetchUserById(id: Long): UserResponse {
        return client.get("/usuarios/{id}") {
            parameter("id", id)
        }.body()
    }

    override suspend fun fetchUserByUsername(username: String): UserResponse {
        return client.get("/usuarios/nombre/{username}") {
            parameter("username", username)
        }.body()
    }
}