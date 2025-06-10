package com.annasolox.kipon.data.repository

import android.util.Log
import com.annasolox.kipon.data.api.models.request.create.LoginRequest
import com.annasolox.kipon.data.api.models.request.create.UserCreate
import com.annasolox.kipon.data.api.service.AuthService
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class AuthRepository(private val authService: AuthService) {
    suspend fun login (loginRequest: LoginRequest): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val token = authService.login(loginRequest)
                Result.success(token)
            } catch (e: IOException) {
                Log.e("Auth Repository", "Error de conexión", e)
                Result.failure(Exception("Error de conexión"))
            } catch (e: ClientRequestException) {
                Log.e("Auth Repository", "Credenciales incorrectas", e)
                Result.failure(Exception("Usuario o contraseña incorrectos"))
            } catch (e: Exception){
                Log.e("Auth Repository", "Error inesperado", e)
                Result.failure(Exception("Error inesperado"))
            }
        }
    }

    suspend fun register(user: UserCreate): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                authService.register(user)
            } catch (e: ResponseException) {
                val errorMessage = runCatching {
                    e.response.bodyAsText()
                }.getOrElse { "Error al leer el mensaje de error" }
                Result.failure(Exception(errorMessage))
            } catch (e: IOException) {
                Result.failure(Exception("Error de conexión"))
            } catch (e: Exception) {
                Result.failure(Exception("Error inesperado: ${e.message}"))
            }
        }
    }
}