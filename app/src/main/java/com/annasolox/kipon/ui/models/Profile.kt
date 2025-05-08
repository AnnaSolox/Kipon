package com.annasolox.kipon.ui.models

import android.net.Uri

data class Profile(
    val id: Long,
    val completeName: String,
    var telephone: String,
    var address: String,
    var photo: Uri
)
