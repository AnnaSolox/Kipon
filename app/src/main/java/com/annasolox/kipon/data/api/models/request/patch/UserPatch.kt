package com.annasolox.kipon.data.api.models.request.patch

import kotlinx.serialization.SerialName

data class UserPatch(
    val password: String,
    val email: String,
    @SerialName("nombre")
    val name: String,
    @SerialName("perfil")
    val profile: ProfilePatch
)
