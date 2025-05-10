package com.annasolox.kipon.ui.models

data class User(
    val id: Long,
    var name: String,
    var email: String,
    val profile: Profile
)
