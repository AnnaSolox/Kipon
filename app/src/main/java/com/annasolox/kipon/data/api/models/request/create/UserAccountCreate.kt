package com.annasolox.kipon.data.api.models.request.create

import kotlinx.serialization.SerialName

data class UserAccountCreate(
    @SerialName("idUsuario")
    val userId: Long,
    @SerialName("idHucha")
    val accountId: Long,
    @SerialName("rol")
    val role: String
)
