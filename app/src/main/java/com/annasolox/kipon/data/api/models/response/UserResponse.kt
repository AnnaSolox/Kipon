package com.annasolox.kipon.data.api.models.response

import com.annasolox.kipon.core.utils.serializers.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class UserResponse(
    val id: Long,
    @SerialName("nombre")
    val name: String,
    val email: String,
    @SerialName("fechaRegistro")
    @Serializable(with = LocalDateSerializer::class)
    val registerDate: LocalDate,
    @SerialName("perfil")
    val profile: UserProfileResponse,
    @SerialName("huchas")
    val accountRole: List<AccountRoleResponse>
)
