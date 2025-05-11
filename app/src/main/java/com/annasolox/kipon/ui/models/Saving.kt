package com.annasolox.kipon.ui.models

import java.time.LocalDate

data class Saving(
    val id: Long,
    val user: UserProfileScreen,
    val amount: Double,
    val date: LocalDate
)
