package com.annasolox.kipon.ui.models

data class UserHomeScreen(
    var userName : String,
    var photoUrl : String?,
    var accounts : List<AccountOverview>
)
