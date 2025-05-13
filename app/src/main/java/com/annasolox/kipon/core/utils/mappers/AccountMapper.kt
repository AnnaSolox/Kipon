package com.annasolox.kipon.core.utils.mappers

import com.annasolox.kipon.data.api.models.response.AccountResponse
import com.annasolox.kipon.data.api.models.response.AccountRoleResponse
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
            dateGoal = accountResponse.goalDate.toString(),
        )
    }

    fun toAccountOverviewFromRole(
        accountRoleResponse: AccountRoleResponse
    ): AccountOverview {
        val account = accountRoleResponse.account
        return AccountOverview (
            id = account.id,
            name = account.name,
            currentMoney = account.currentMoney,
            moneyGoal = account.moneyGoal,
            dateGoal = account.dateGoal.toString(),
        )
    }
}