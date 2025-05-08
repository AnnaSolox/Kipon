package com.annasolox.kipon.data.api.models.request.update

import kotlinx.serialization.SerialName

data class UserUpdate(
    val password: String,
    val email: String,
    @SerialName("nombre")
    val name: String,
    @SerialName("perfil")
    val profile: ProfileUpdate
)
