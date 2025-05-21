package com.annasolox.kipon.data.api.models.request.patch

import com.annasolox.kipon.core.utils.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class AccountPatch(
    @SerialName("nombre")
    val name: String? = null,
    @SerialName("objetivoAhorro")
    val moneyGoal: Double? = null,
    @SerialName("fechaObjetivo")
    @Serializable(with = LocalDateSerializer::class)
    val dateGoal: LocalDate? = null,
    @SerialName("idAdministrador")
    val adminId: Long? = null,
    @SerialName("fotoHucha")
    val photo: String? = null
)
