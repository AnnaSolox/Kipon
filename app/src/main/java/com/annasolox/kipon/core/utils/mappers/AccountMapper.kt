package com.annasolox.kipon.core.utils.mappers

import com.annasolox.kipon.data.api.models.response.AccountResponse
import com.annasolox.kipon.data.api.models.response.AccountRoleResponse
import com.annasolox.kipon.data.api.models.response.SavingResponse
import com.annasolox.kipon.ui.models.AccountDetails
import com.annasolox.kipon.ui.models.AccountOverview
import com.annasolox.kipon.ui.models.Saving

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
            photo = accountResponse.photo
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
            photo = account.photo,
        )
    }

    fun toDetailAccount(
        accountResponse: AccountResponse
    ): AccountDetails {
        return AccountDetails(
            id = accountResponse.id,
            name = accountResponse.name,
            currentAmount = accountResponse.currentMoney,
            moneyGoal = accountResponse.moneyGoal,
            dateGoal = accountResponse.goalDate.toString(),
            userMembers = accountResponse.userMembers.map { it.user},
            admin = accountResponse.admin,
            savings = accountResponse.savings.map { toSavingUi(it)},
            photo = accountResponse.photo
        )
    }

    fun toSavingUi(
        savingResponse: SavingResponse
    ): Saving {
        return Saving(
            id = savingResponse.id,
            user = savingResponse.user,
            date = savingResponse.date.toString(),
            amount = savingResponse.amount
        )
    }
}