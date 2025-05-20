package com.annasolox.kipon.data.api.service

import android.util.Log
import com.annasolox.kipon.data.api.models.request.patch.UserPatch
import com.annasolox.kipon.data.api.models.response.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class UserServiceImpl(private val client: HttpClient) : UserService {

    override suspend fun fetchUserById(id: Long): UserResponse {
        return client.get("usuarios/{id}") {
            parameter("id", id)
        }.body()
    }

    override suspend fun fetchUserByUsername(username: String): UserResponse {
        val response = client.get("usuarios/nombre/$username")
        if (!response.status.isSuccess()) {
            val error = response.bodyAsText()
            throw Exception("Error al obtener usuario: $error")
        }
        Log.d("UserService", response.bodyAsText())
        return response.body()
    }

    override suspend fun updateUserInformation(id: Long, userPatch: UserPatch): UserResponse {
        val response = client.patch("usuarios/$id") {
            contentType(Json)
            setBody(userPatch)
        }

        if (!response.status.isSuccess()) {
            val error = response.bodyAsText()
            throw Exception("Error al actualizar usuario: $error")
        }

        return response.body()
    }

    override suspend fun fetchUserByPartialUsername(search: String): List<UserResponse> {
        val response = client.get("usuarios/nombre"){
            url {
                parameters.append("contiene", search)
            }
        }

        if (!response.status.isSuccess()) {
            val error = response.bodyAsText()
            throw Exception("Error al buscar usuarios: $error")
        }
        Log.d("UserService", "Fetched users: ${response.bodyAsText()}")
        return response.body()
    }
}