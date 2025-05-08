package com.annasolox.kipon.ui.models

import java.time.LocalDate

data class AccountDetails(
    val id: Long,
    var name: String,
    var currentAmount: Double,
    var moneyGoal: Double,
    var dateGoal: LocalDate?,
    val userMembers: ArrayList<User>,
    var admin: User,
    var savings: List<Saving>
)
