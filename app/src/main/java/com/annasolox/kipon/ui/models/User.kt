package com.annasolox.kipon.ui.models

data class User(
    val id: Long,
    var name: String,
    var email: String,
    var password: String,
    val profile: Profile
)
