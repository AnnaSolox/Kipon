package com.annasolox.kipon.ui.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasolox.kipon.core.utils.mappers.UserMapper.toUserHomeScreen
import com.annasolox.kipon.core.utils.mappers.UserMapper.toUserProfileScreenFromUserResponse
import com.annasolox.kipon.data.repository.UserRepository
import com.annasolox.kipon.ui.models.UserHomeScreen
import com.annasolox.kipon.ui.models.UserProfileScreen
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _userHome = MutableLiveData<UserHomeScreen>()
    val userHome: LiveData<UserHomeScreen> get() = _userHome
    private val _userProfile = MutableLiveData<UserProfileScreen>()
    val userProfile: LiveData<UserProfileScreen> get() = _userProfile

    init {
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            val username = sharedPreferences.getString("username", null)
            username?.let {
                try {
                    val response = userRepository.getUserByUsername(it)
                    _userHome.value = toUserHomeScreen(response)
                    _userProfile.value = toUserProfileScreenFromUserResponse(response)
                } catch (e: Exception) {
                    Log.e("UserViewModel", "Error loading user: ${e.message}")
                }
            }
        }
    }
}