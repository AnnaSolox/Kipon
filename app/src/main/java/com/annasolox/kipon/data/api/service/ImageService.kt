package com.annasolox.kipon.data.api.service

interface ImageService {
    suspend fun uploadImage(image: ByteArray): String
}