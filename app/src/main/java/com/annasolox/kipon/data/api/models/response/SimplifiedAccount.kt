package com.annasolox.kipon.data.api.models.response

import kotlinx.serialization.SerialName

data class SimplifiedAccount(
    val id: Long,
    @SerialName("nombre")
    val name: String,
    @SerialName("cantidadTotal")
    val currentMoney: Double,
    @SerialName("objetivoAhorro")
    val moneyGoal: Double
)
