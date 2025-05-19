package com.annasolox.kipon.data.api.models.request.patch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPatch(
    val password: String? = null,
    val email: String? = null,
    @SerialName("nombre")
    val name: String? = null,
    @SerialName("perfil")
    val profile: ProfilePatch? = null,
)
