package com.annasolox.kipon.data.api.models.request.create

import com.annasolox.kipon.core.utils.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class UserCreate(
    @SerialName("nombre")
    var name: String,
    var password: String,
    var email: String,
    @SerialName("fechaRegistro")
    @Serializable(with = LocalDateSerializer::class)
    val registerDate: LocalDate,
    @SerialName("perfil")
    val profile: ProfileCreate
)