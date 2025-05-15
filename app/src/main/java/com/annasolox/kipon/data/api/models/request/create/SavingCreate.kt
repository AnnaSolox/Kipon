package com.annasolox.kipon.data.api.models.request.create

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavingCreate(
    @SerialName("idUsuario")
    val userId: Long,
    @SerialName("cantidad")
    val amount: Double
)
