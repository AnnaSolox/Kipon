package com.annasolox.kipon.data.api.models.response

import com.annasolox.kipon.core.utils.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class SavingResponse(
    val id: Long,
    @SerialName("nombreUsuario")
    val userName: String,
    @SerialName("nombreHucha")
    val accountName: String,
    @SerialName("idUsuario")
    val userId: Long,
    @SerialName("cantidad")
    val amount: Double,
    @SerialName("fecha")
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    @SerialName("fotoUsuario")
    val userPhoto: String?,
    @SerialName("fotoHucha")
    val accountPhoto: String?,
    @SerialName("saldoPosterior")
    val currentMoney: Double
)