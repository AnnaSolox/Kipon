package com.annasolox.kipon.data.api.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountRoleResponse(
    @SerialName("hucha")
    val account: SimplifiedAccountResponse,
    @SerialName("rol")
    val role: String
)
