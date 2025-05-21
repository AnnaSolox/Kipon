package com.annasolox.kipon.data.api.service

import androidx.compose.material3.ExposedDropdownMenuBox
import com.annasolox.kipon.core.TokenProvider
import com.annasolox.kipon.data.api.models.request.create.LoginRequest
import com.annasolox.kipon.data.api.models.request.create.UserCreate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.errors.IOException

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

    override suspend fun register(userCreate: UserCreate): Result<Unit> {
        return try {
            client.post("auth/register") {
                setBody(userCreate)
                expectSuccess = true
            }
            Result.success(Unit)
        } catch (e: ResponseException) {
            // Para errores HTTP 4xx y 5xx
            val errorBody = try {
                e.response.bodyAsText()
            } catch (ex: Exception) {
                "No se pudo leer el mensaje de error"
            }
            Result.failure(Exception("Error HTTP ${e.response.status.value}: $errorBody"))
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }
}
