package com.annasolox.kipon.domain.auth

import com.annasolox.kipon.data.api.models.request.create.ProfileCreate
import com.annasolox.kipon.data.api.models.request.create.UserCreate
import com.annasolox.kipon.data.repository.AuthRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate

class RegisterUseCase(
    private val authRepository: AuthRepository
) {

    private val mutex = Mutex()

    data class Result(
        val success: Boolean,
        val usernameError: String? = null,
        val passwordError: String? = null,
        val passwordConfirmationError: String? = null,
        val emailError: String? = null,
        val completeNameError: String? = null,
        val phoneError: String? = null,
        val addressError: String? = null,
        val message: String? = null
    )


    suspend operator fun invoke(
        username: String?,
        password: String?,
        passwordConfirmation: String?,
        email: String?,
        completeName: String?,
        phone: String?,
        address: String?,
        photo: String?
    ): Result {
        var success = true
        var usernameErr: String? = null
        var passwordErr: String? = null
        var passwordConfirmationErr: String? = null
        var emailErr: String? = null
        var completeNameErr: String? = null
        var phoneErr: String? = null
        var addressErr: String? = null

        if (username.isNullOrBlank()) {
            usernameErr = "Nombre de usuario obligatorio"
            success = false
        } else if (username.length > 50) {
            usernameErr = "Debe tener menos de 50 caracteres"
            success = false
        }

        if (password.isNullOrBlank()) {
            passwordErr = "Contraseña obligatoria"
            success = false
        } else if (password.length < 8) {
            passwordErr = "Debe tener al menos 8 caracteres"
            success = false
        } else if (password.length > 50) {
            passwordErr = "Debe tener menos de 50 caracteres"
            success = false
        }

        if (passwordConfirmation.isNullOrBlank() || passwordConfirmation != password) {
            passwordConfirmationErr = "Las contraseñas no coinciden"
            success = false
        }

        if (email.isNullOrBlank()) {
            emailErr = "Correo electrónico obligatorio"
            success = false
        } else if (!email.contains("@")) {
            emailErr = "Correo electrónico no válido"
            success = false
        }

        if (completeName.isNullOrBlank()) {
            completeNameErr = "Nombre completo obligatorio"
            success = false
        } else if (completeName.length > 100) {
            completeNameErr = "Debe tener menos de 100 caracteres"
            success = false
        }

        if (phone.isNullOrBlank()) {
            phoneErr = "Teléfono obligatorio"
            success = false
        } else if (!phone.matches(Regex("[0-9]+")) || phone.length != 9) {
            phoneErr = "Teléfono no válido"
            success = false
        }

        if (address.isNullOrBlank()) {
            addressErr = "Dirección obligatoria"
            success = false
        } else if (address.length > 50) {
            addressErr = "Debe tener menos de 50 caracteres"
            success = false
        }

        if (!success) {
            return Result(
                success = false,
                usernameError = usernameErr,
                passwordError = passwordErr,
                passwordConfirmationError = passwordConfirmationErr,
                emailError = emailErr,
                completeNameError = completeNameErr,
                phoneError = phoneErr,
                addressError = addressErr
            )
        }

        return try {
            mutex.withLock {
                val request = UserCreate(
                    name = username!!,
                    password = password!!,
                    email = email!!,
                    profile = ProfileCreate(
                        completeName = completeName!!,
                        telephone = phone!!,
                        address = address!!,
                        photo = photo
                    ),
                    registerDate = LocalDate.now()
                )
                val backendResult = authRepository.register(request) // Result<Unit>

                backendResult.fold(
                    onSuccess = {
                        Result(success = true)
                    },
                    onFailure = { error ->
                        Result(
                            success = false,
                            message = error.message ?: "Error desconocido"
                        )
                    }
                )
            }
        } catch (e: Exception) {
            Result(success = false, message = e.message ?: "Error desconocido")
        }
    }
}