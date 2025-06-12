package com.annasolox.kipon.domain.image

import com.annasolox.kipon.data.repository.ImageUploadRepository

class UploadImageUseCase(
    private val imageUploadRepository: ImageUploadRepository
) {
    suspend operator fun invoke (image: ByteArray, type: String): Result<String> {
        return try {
            val imageUrl = imageUploadRepository.uploadImage(image, type)
            Result.success(imageUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}