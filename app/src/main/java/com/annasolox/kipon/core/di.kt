package com.annasolox.kipon.core

import android.content.Context
import androidx.navigation.NavController
import com.annasolox.kipon.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single {
        val tokenProvider: TokenProvider = get()

        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }, contentType = ContentType.Application.Json)
            }

            install(Logging) {
                logger = object: Logger {
                    override fun log(message: String) {
                        println("Ktor: $message")
                    }
                }
                level = LogLevel.ALL
            }

            defaultRequest {
                url(urlString = "http://${BuildConfig.HOST_URL}:${BuildConfig.HOST_PORT}/kipon/")
                contentType(ContentType.Application.Json)
                header("Accept", "application/json")
                tokenProvider.token?.let {
                    header("Authorization", "Bearer $it")
                }
            }
        }
    }

    // Inyectar el token si lo necesitas desde Koin
    single { TokenProvider() }

    // Inyectar sharedpreferences
    single { androidContext().getSharedPreferences("KiponPrefs", Context.MODE_PRIVATE) }
}