package com.annasolox.kipon.ui.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasolox.kipon.data.api.models.response.UserResponse
import com.annasolox.kipon.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(
    userRepository: UserRepository,
    sharedPreferences: SharedPreferences
) : ViewModel(){
    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse>  get() = _user

    init {
        viewModelScope.launch {
            val username = sharedPreferences.getString("username", null)
            username?.let {
                val response = userRepository.getUserByUsername(it)
                _user.value = response
            }
        }
    }

}