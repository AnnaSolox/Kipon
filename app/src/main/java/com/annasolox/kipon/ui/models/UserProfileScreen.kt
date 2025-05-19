package com.annasolox.kipon.ui.models

import java.time.LocalDate

data class UserProfileScreen(
    val id: Long,
    var name: String,
    var email: String,
    val profile: Profile,
    val registrationDate: LocalDate
)
