package com.annasolox.kipon.ui.models

data class Saving(
    val id: Long,
    val userId: Long,
    val username: String,
    val accountName:String,
    val amount: Double,
    val currentMoney: Double,
    val date: String,
    val userPhoto: String?,
    val accountPhoto: String?
)
