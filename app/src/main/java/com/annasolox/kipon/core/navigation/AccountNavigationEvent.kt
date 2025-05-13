package com.annasolox.kipon.core.navigation

sealed class AccountNavigationEvent {
    object NavigateToAccountDetail: AccountNavigationEvent()
}