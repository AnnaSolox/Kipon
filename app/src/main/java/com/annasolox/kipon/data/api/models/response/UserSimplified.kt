package com.annasolox.kipon.data.api.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSimplified (
    val id: Long,
    @SerialName("nombre")
    val name: String? = null,
    @SerialName("foto")
    val photo: String? = null,
)