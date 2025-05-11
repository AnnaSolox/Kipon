package com.annasolox.kipon.data.api.models.response

import kotlinx.serialization.Serializable

@Serializable
data class UserRolResponse(
    val user: UserSimplified,
    val role : String
)