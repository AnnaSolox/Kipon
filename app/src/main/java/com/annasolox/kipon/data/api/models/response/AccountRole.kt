package com.annasolox.kipon.data.api.models.response

import kotlinx.serialization.SerialName

data class AccountRole(
    @SerialName("hucha")
    val account: SimplifiedAccount,
    @SerialName("rol")
    val role: String
)
