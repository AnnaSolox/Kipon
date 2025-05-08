package com.annasolox.kipon.data.api.models.response

import com.annasolox.kipon.data.api.utils.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class Saving(
    val id: Long,
    @SerialName("usuario")
    val user: User,
    @SerialName("cantidad")
    val amount: Double,
    @SerialName("fecha")
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate
)