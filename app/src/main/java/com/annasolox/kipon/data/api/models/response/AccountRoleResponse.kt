package com.annasolox.kipon.data.api.models.response

import kotlinx.serialization.SerialName

data class AccountRoleResponse(
    @SerialName("hucha")
    val account: SimplifiedAccountResponse,
    @SerialName("rol")
    val role: String
)
