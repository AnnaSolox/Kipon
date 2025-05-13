package com.annasolox.kipon.ui.models

import com.annasolox.kipon.data.api.models.response.UserSimplified
import java.time.LocalDate

data class AccountDetails(
    val id: Long,
    var name: String,
    var currentAmount: Double,
    var moneyGoal: Double,
    var dateGoal: String,
    var photo: String?,
    val userMembers: List<UserSimplified>,
    var admin: String,
    var savings: List<Saving>
)
