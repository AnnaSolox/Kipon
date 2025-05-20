package com.annasolox.kipon.data.repository

import com.annasolox.kipon.data.api.service.ImageService

class ImageUploadRepository(private val imageService: ImageService){
    suspend fun uploadImage(image: ByteArray): String {
        return imageService.uploadImage(image)
    }
}