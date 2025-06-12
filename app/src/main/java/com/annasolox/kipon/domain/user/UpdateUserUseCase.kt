package com.annasolox.kipon.domain.user

import android.util.Log
import com.annasolox.kipon.data.api.models.request.patch.ProfilePatch
import com.annasolox.kipon.data.api.models.request.patch.UserPatch
import com.annasolox.kipon.data.repository.UserRepository

class UpdateUserUseCase(
    private val userRepository: UserRepository
) {

    data class Result(
        val success: Boolean,
        val emailError: String? = null,
        val phoneError: String? = null,
        val addressError: String? = null
    )

    suspend operator fun invoke(
        email: String?,
        phone: String?,
        address: String?,
        photo: String?
    ): Result {
        var success = true
        var emailErr: String? = null
        var phoneErr: String? = null
        var addressErr: String? = null

        if (email.isNullOrBlank() || !email.contains("@") || !email.contains(".")) {
            emailErr = "El formato del email es inválido"
            success = false
        }

        if (!phone.isNullOrBlank() && !Regex("^[0-9]*$").matches(phone)) {
            phoneErr = "El teléfono solo puede contener números"
            success = false
        }

        if (!address.isNullOrBlank() && address.length > 50) {
            addressErr = "La dirección no puede superar los 50 caracteres"
            success = false
        }

        if (!success) {
            return Result(false, emailErr, phoneErr, addressErr)
        }

        return try {
            val patch = UserPatch(
                email = email,
                profile = ProfilePatch(
                    telephone = phone,
                    address = address,
                    photo = photo
                )
            )

            userRepository.updateCurrentUser(patch)
            Result(true)
        } catch (e: Exception) {
            Log.e("UpdateUserUseCase", "${e.message}")
            Result(false)
        }
    }
}