package com.annasolox.kipon.ui.models

import com.annasolox.kipon.data.api.models.response.UserSimplified

data class Saving(
    val id: Long,
    val user: UserSimplified,
    val amount: Double,
    val date: String
)
