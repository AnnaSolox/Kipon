package com.annasolox.kipon.domain.auth

import android.content.SharedPreferences
import androidx.core.content.edit

class SaveUserNameUSeCase(
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(username: String) {
        sharedPreferences.edit { putString("username", username) }
    }
}