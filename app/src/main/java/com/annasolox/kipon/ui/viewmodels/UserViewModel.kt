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
import com.annasolox.kipon.ui.models.AccountOverview
import com.annasolox.kipon.ui.models.Saving
import com.annasolox.kipon.ui.models.UserHomeScreen
import com.annasolox.kipon.ui.models.UserProfileScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _userHome = MutableLiveData<UserHomeScreen>()
    val userHome: LiveData<UserHomeScreen> get() = _userHome
    private val _userProfile = MutableLiveData<UserProfileScreen>()
    val userProfile: LiveData<UserProfileScreen> get() = _userProfile

    private val _allUserSavings = MutableLiveData<List<Saving>>()
    val allUserSavings: LiveData<List<Saving>> get() = _allUserSavings

    fun addAccountToUserAccountsList(accountOverview: AccountOverview){
        val currentUser = _userHome.value
        Log.d("UserViewModel", "Current User: $currentUser")
        if (currentUser != null) {
            val updatedAccounts = currentUser.accounts.toMutableList().apply {
                add(accountOverview)
            }
            Log.d("UserViewModel", "Updated Accounts: $updatedAccounts")
            val updatedUser = currentUser.copy(accounts = updatedAccounts)
            _userHome.postValue(updatedUser)
        } else {
            Log.d("UserViewModel", "User is null")
        }
    }

    fun loadUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val username = sharedPreferences.getString("username", null)
            username?.let {
                try {
                    val response = userRepository.getUserByUsername(it)
                    withContext(Dispatchers.Main) {
                        _userHome.value = toUserHomeScreen(response)
                    }
                    _userProfile.postValue(toUserProfileScreenFromUserResponse(response))
                } catch (e: Exception) {
                    Log.e("UserViewModel", "Error loading user: ${e.message}")
                }
            }
        }
    }

    fun getSavingsFromUser(){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("UserViewModel", "Userhome: ${_userHome.value}")
            Log.d("UserViewModel", "Getting savings from user")
            _userHome.value?.savings?.let {
                val savings = _userHome.value?.savings
                savings?.let { _allUserSavings.postValue(savings) }
                Log.d("UserViewModel", "Savings: $savings")
            }
        }
    }
}