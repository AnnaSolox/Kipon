package com.annasolox.kipon.data.api.models.request.patch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfilePatch(
    @SerialName("telefono")
    val telephone: String? = null,
    @SerialName("direccion")
    val address: String? = null,
    @SerialName("fotoPerfil")
    val photo: String? = null
)
