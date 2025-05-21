package com.annasolox.kipon.data.api.service

interface ImageService {
    suspend fun uploadImage(image: ByteArray, purpose: String): String
    suspend fun deleteImage(key: String)
}