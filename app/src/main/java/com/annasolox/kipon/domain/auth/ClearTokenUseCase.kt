package com.annasolox.kipon.domain.auth

import android.content.SharedPreferences
import androidx.core.content.edit

class ClearTokenUseCase (
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke() {
        sharedPreferences.edit { remove("auth_token") }
    }
}