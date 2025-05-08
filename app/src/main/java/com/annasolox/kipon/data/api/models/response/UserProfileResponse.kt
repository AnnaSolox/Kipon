package com.annasolox.kipon.data.api.models.response

import kotlinx.serialization.SerialName

data class UserProfileResponse(
    val id: Long,
    @SerialName("nombreCompleto")
    val completeName: String,
    @SerialName("telefono")
    val telephone: String,
    @SerialName("direccion")
    val address: String
)
