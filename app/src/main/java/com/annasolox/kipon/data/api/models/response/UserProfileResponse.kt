package com.annasolox.kipon.data.api.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    val id: Long,
    @SerialName("nombreCompleto")
    val completeName: String,
    @SerialName("telefono")
    val telephone: String,
    @SerialName("direccion")
    val address: String,
    @SerialName("fotoPerfil")
    val photo: String?
)
