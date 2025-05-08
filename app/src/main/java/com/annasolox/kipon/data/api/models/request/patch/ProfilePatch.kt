package com.annasolox.kipon.data.api.models.request.patch

import kotlinx.serialization.SerialName

data class ProfilePatch(
    @SerialName("telefono")
    val telephone: String,
    @SerialName("direccion")
    val address: String
)
