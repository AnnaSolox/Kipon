package com.annasolox.kipon.ui.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasolox.kipon.core.utils.mappers.UserMapper.toUserHomeScreen
import com.annasolox.kipon.core.utils.mappers.UserMapper.toUserProfileScreen
import com.annasolox.kipon.data.repository.UserRepository
import com.annasolox.kipon.ui.models.UserHomeScreen
import com.annasolox.kipon.ui.models.UserProfileScreen
import kotlinx.coroutines.launch

class UserViewModel(
    userRepository: UserRepository,
    sharedPreferences: SharedPreferences
) : ViewModel(){
    private val _userHome = MutableLiveData<UserHomeScreen>()
    val userHome: LiveData<UserHomeScreen>  get() = _userHome
    private val _userProfile = MutableLiveData<UserProfileScreen>()
    val userProfile: LiveData<UserProfileScreen> get() = _userProfile

    init {
        viewModelScope.launch {
            val username = sharedPreferences.getString("username", null)
            username?.let {
                val response = userRepository.getUserByUsername(it)
                _userHome.value = toUserHomeScreen(response)
                _userProfile.value = toUserProfileScreen(response)
            }
        }
    }

}