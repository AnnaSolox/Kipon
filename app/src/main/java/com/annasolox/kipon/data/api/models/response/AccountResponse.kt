package com.annasolox.kipon.data.api.models.response

import com.annasolox.kipon.data.api.utils.serializers.LocalDateSerializer
import com.annasolox.kipon.data.api.utils.serializers.LocalDateTimeSerializer
import com.annasolox.kipon.ui.models.AccountDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class AccountResponse(
    val id: Long,
    @SerialName("nombre")
    var name: String,
    @SerialName("cantidadTotal")
    val currentMoney: Double,
    @SerialName("objetivoAhorro")
    val moneyGoal: Double,
    @SerialName("fechaCreacion")
    @Serializable(with = LocalDateTimeSerializer::class)
    val creationDate: LocalDateTime,
    @SerialName("fechaObjetivo")
    @Serializable(with = LocalDateSerializer::class)
    val goalDate: LocalDate,
    @SerialName("administrador")
    val admin: String,
    @SerialName("ahorros")
    val savings: List<SavingResponse>
)
