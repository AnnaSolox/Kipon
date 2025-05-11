package com.annasolox.kipon.data.api.models.response

import kotlinx.serialization.Serializable

@Serializable
data class UserSimplified (
    val id: Long,
    val name: String
)