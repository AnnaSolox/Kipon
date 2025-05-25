package com.annasolox.kipon

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createMockClient(): HttpClient {
    return HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                when (request.url.fullPath) {
                    "/usuario/actual" -> {
                        respond(
                            content = """{ "nombre": "Laura", "email": "laura@email.com" }""",
                            status = HttpStatusCode.OK,
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                        )
                    }
                    else -> error("Unhandled request: ${request.url}")
                }
            }
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
}