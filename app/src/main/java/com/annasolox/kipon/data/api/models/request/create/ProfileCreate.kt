package com.annasolox.kipon.data.api.models.request.create

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileCreate(
    @SerialName("nombreCompleto")
    var completeName: String,
    @SerialName("telefono")
    var telephone: String,
    @SerialName("direccion")
    var address: String,
    @SerialName("foto")
    val photo: String? = null
)
