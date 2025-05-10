package com.annasolox.kipon.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClientProvider {
    var authToken: String? = null

    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json = Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            }, contentType = ContentType.Any)
        }

        install(Logging) {
            level = LogLevel.BODY
        }

        defaultRequest {
            url {
                protocol = io.ktor.http.URLProtocol.HTTPS
                host = "localhost:8080/kipon"
                header("Accept", "application/json")
                authToken?.let { header("Authorization", "Bearer $it" ) }
            }
        }
    }
}