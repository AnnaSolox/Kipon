package com.annasolox.kipon.domain.auth

import android.content.SharedPreferences
import androidx.core.content.edit

class SaveTokenUseCase(private val sharedPreferences: SharedPreferences) {
    operator fun invoke(token: String) {
        sharedPreferences.edit { putString("auth_token", token) }
    }
}