package com.annasolox.kipon.data.api.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimplifiedAccountResponse(
    val id: Long,
    @SerialName("nombre")
    val name: String,
    @SerialName("cantidadTotal")
    val currentMoney: Double,
    @SerialName("fechaObjetivo")
    val dateGoal: String?,
    @SerialName("objetivoAhorro")
    val moneyGoal: Double,
    @SerialName("fotoHucha")
    val photo: String?,
)
