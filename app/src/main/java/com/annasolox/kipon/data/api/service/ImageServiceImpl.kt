package com.annasolox.kipon.data.api.service

import io.ktor.client.HttpClient
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

class ImageServiceImpl(private val client: HttpClient): ImageService{
    override suspend fun uploadImage(image: ByteArray, purpose: String): String {
        val response = client.post("images/upload") {
            setBody(MultiPartFormDataContent(
                formData {
                    append("file", image, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                    })
                    append("purpose", purpose)
                }
            ))
            expectSuccess = false
        }

        val responseBody = response.bodyAsText()

        if(!response.status.isSuccess()) {
            val error = response.bodyAsText()
            throw Exception("Error al subir la imagen: $error")
        }

        return responseBody
    }

    override suspend fun deleteImage(key: String) {
        val response = client.delete("images/delete") {
            url {
                parameters.append("key", key)
            }
        }

        if (!response.status.isSuccess()) {
            val error = response.bodyAsText()
            throw Exception("Error al eliminar la imagen: $error")
        }
    }
}