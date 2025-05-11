package com.annasolox.kipon.ui.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.annasolox.kipon.data.repository.UserRepository

class UserViewModel(
    userRepository: UserRepository,
    sharedPreferences: SharedPreferences
) : ViewModel(){

}