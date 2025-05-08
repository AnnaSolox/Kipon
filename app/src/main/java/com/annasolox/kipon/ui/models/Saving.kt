package com.annasolox.kipon.ui.models

import java.time.LocalDate

data class Saving(
    val id: Long,
    val user: User,
    val amount: Double,
    val date: LocalDate
)
