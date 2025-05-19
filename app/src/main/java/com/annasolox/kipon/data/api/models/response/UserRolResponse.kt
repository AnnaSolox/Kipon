package com.annasolox.kipon.data.api.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRolResponse(
    @SerialName("usuario")
    val user: UserSimplified,
    @SerialName("rol")
    val role : String,
)