package com.annasolox.kipon.data.api.utils.mappers

import com.annasolox.kipon.data.api.models.response.AccountResponse
import com.annasolox.kipon.ui.models.AccountOverview

object AccountMapper {
    fun toAccountOverview(
        accountResponse: AccountResponse
    ): AccountOverview {
        return AccountOverview(
            id = accountResponse.id,
            name = accountResponse.name,
            currentMoney = accountResponse.currentMoney,
            moneyGoal = accountResponse.moneyGoal,
            userMembers = accountResponse.userMembers.map { it.user.name }.toCollection(ArrayList()),
            admin = accountResponse.userMembers.first { it.role == "Administrador" }.user.name
        )
    }
}