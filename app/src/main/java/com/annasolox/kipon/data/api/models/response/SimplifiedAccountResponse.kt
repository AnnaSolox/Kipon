package com.annasolox.kipon.data.api.models.response

import com.annasolox.kipon.core.utils.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class SimplifiedAccountResponse(
    val id: Long,
    @SerialName("nombre")
    val name: String,
    @SerialName("cantidadTotal")
    val currentMoney: Double,
    @SerialName("fechaObjetivo")
    @Serializable(with = LocalDateSerializer::class)
    val dateGoal: LocalDate? = null,
    @SerialName("objetivoAhorro")
    val moneyGoal: Double,
    @SerialName("fotoHucha")
    val photo: String?,
)
