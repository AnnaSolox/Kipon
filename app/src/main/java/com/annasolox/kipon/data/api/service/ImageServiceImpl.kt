package com.annasolox.kipon.data.api.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.http.isSuccess
import java.io.File

class ImageServiceImpl(private val client: HttpClient): ImageService{
    override suspend fun uploadImage(image: ByteArray): String {
        val response = client.post("images/upload") {
            setBody(MultiPartFormDataContent(
                formData {
                    append("file", image, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                    })
                }
            ))
        }

        if(!response.status.isSuccess()) {
            val error = response.bodyAsText()
            throw Exception("Error al subir la imagen: $error")
        }

        return response.bodyAsText()
    }

}