package com.annasolox.kipon.ui.models

data class AccountOverview(
    val id: Long,
    var name: String,
    var currentMoney: Double,
    var moneyGoal: Double,
    val userMembers: ArrayList<String>,
    var admin: String
)
