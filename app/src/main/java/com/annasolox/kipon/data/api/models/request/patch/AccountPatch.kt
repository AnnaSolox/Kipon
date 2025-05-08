package com.annasolox.kipon.data.api.models.request.patch

import com.annasolox.kipon.data.api.utils.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class AccountPatch(
    @SerialName("nombre")
    val name: String,
    @SerialName("objetivoAhorro")
    val moneyGoal: Double,
    @SerialName("fechaObjetivo")
    @Serializable(with = LocalDateSerializer::class)
    val dateGoal: LocalDate,
    @SerialName("idAdminitrador")
    val adminId: Long
)
