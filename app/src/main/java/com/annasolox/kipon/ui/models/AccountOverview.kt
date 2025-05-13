package com.annasolox.kipon.ui.models

data class AccountOverview(
    val id: Long,
    var name: String,
    var dateGoal: String,
    var currentMoney: Double,
    var photo: String?,
    var moneyGoal: Double
)
