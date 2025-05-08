package com.annasolox.kipon.data.api.models.request.update

import kotlinx.serialization.SerialName

data class ProfileUpdate(
    @SerialName("telefono")
    val telephone: String,
    @SerialName("direccion")
    val address: String
)
