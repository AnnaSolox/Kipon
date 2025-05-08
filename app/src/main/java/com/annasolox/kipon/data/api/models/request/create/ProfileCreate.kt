package com.annasolox.kipon.data.api.models.request.create

import kotlinx.serialization.SerialName

data class ProfileCreate(
    @SerialName("nombreCompleto")
    val completeName: String,
    @SerialName("telefono")
    val telephone: String,
    @SerialName("address")
    val address: String
)
