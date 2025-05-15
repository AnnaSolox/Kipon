package com.annasolox.kipon.ui.models

data class Saving(
    val id: Long,
    val user: String,
    val amount: Double,
    val currentMoney: Double,
    val date: String,
    val photo: String?
)
