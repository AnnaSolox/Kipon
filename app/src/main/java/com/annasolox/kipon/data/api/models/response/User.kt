package com.annasolox.kipon.data.api.models.response

import com.annasolox.kipon.data.api.utils.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class User(
    val id: Long,
    @SerialName("nombre")
    val name: String,
    val email: String,
    @SerialName("fechaRegistro")
    @Serializable(with = LocalDateSerializer::class)
    val registerDate: LocalDate,
    @SerialName("perfil")
    val profile: UserProfile,
    @SerialName("huchas")
    val accountRole: List<AccountRole>
)
