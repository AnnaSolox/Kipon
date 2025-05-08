package com.annasolox.kipon.data.api.models.request.create

import kotlinx.serialization.SerialName

data class SavingCreate(
    val id: Long,
    @SerialName("cantidad")
    val amount: Double
)
