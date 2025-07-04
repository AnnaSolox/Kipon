package com.annasolox.kipon.data.api.models.request.create

import com.annasolox.kipon.core.utils.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class AccountCreate(
    @SerialName("nombre")
    val name: String,
    @SerialName("objetivoAhorro")
    val moneyGoal: Double,
    @SerialName("fechaObjetivo")
    @Serializable(with = LocalDateSerializer::class)
    val dateGoal: LocalDate?,
    @SerialName("fotoHucha")
    val photo: String? = null
)
