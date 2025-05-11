package com.annasolox.kipon.core.navigation

sealed class LoginNavigationEvent {
    object NavigateToHome: LoginNavigationEvent()
}