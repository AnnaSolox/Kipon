package com.annasolox.kipon.ui.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasolox.kipon.core.utils.mappers.UserMapper.toUserHomeScreen
import com.annasolox.kipon.core.utils.mappers.UserMapper.toUserProfileScreenFromUserResponse
import com.annasolox.kipon.data.api.models.request.patch.ProfilePatch
import com.annasolox.kipon.data.api.models.request.patch.UserPatch
import com.annasolox.kipon.data.repository.UserRepository
import com.annasolox.kipon.ui.models.AccountOverview
import com.annasolox.kipon.ui.models.Saving
import com.annasolox.kipon.ui.models.SearchedUser
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

    // Profile screen data
    private val _username = MutableLiveData<String?>(_userProfile.value?.name ?: "")
    val username: LiveData<String?> get() = _username
    fun onUsernameChanged(username: String) {
        _username.value = username
    }
    private val _userNameError = MutableLiveData<String?>()
    val userNameError: LiveData<String?> get() = _userNameError

    private val _email = MutableLiveData<String>(_userProfile.value?.email ?: "")
    val email: LiveData<String?> get() = _email
    fun onEmailChanged(email: String) {
        _email.value = email
    }
    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> get() = _emailError

    private val _phone = MutableLiveData<String?>(_userProfile.value?.profile?.telephone ?: "")
    val phone: LiveData<String?> get() = _phone
    fun onPhoneChanged(phone: String) {
        _phone.value = phone
    }
    private val _phoneError = MutableLiveData<String?>()
    val phoneError: LiveData<String?> get() = _phoneError
    private val _address = MutableLiveData<String>(_userProfile.value?.profile?.address ?: "")
    val address: LiveData<String?> get() = _address
    fun onAddressChanged(address: String) {
        _address.value = address
    }
    private val _addressError = MutableLiveData<String?>()
    val addressError: LiveData<String?> get() = _addressError

    private val _photo = MutableLiveData<String?>(_userProfile.value?.profile?.photo ?: "")
    val photo: LiveData<String?> get() = _photo
    fun onPhotoChanged(photo: String) {
        _photo.value = photo
    }
    private val _password = MutableLiveData<String>()
    val password: LiveData<String?> get() = _password
    fun onPasswordChanged(password: String) {
        _password.value = password
    }
    private val _passwordConfirmation = MutableLiveData<String?>()
    val passwordConfirmation: LiveData<String?> get() = _passwordConfirmation
    fun onPasswordConfirmationChanged(passwordConfirmation: String) {
        _passwordConfirmation.value = passwordConfirmation
    }
    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> get() = _passwordError

    private val _fetchedUsers = MutableLiveData<List<SearchedUser>>()
    val fetchedUsers: LiveData<List<SearchedUser>> get() = _fetchedUsers
    private val _fetchedUsersError = MutableLiveData<String?>(null)
    val fetchedUsersError: LiveData<String?> get() = _fetchedUsersError

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

    fun searchUsers(partialName: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val users = userRepository.fetchUsersByPartialName(partialName)
                withContext(Dispatchers.Main) {
                    _fetchedUsers.value = users
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main) {
                    _fetchedUsersError.value = "No se han encontrado usuarios."
                }
                Log.e("UserViewModel", "Error buscando usuarios: ${e.message}")
            }

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
                        _userProfile.value = toUserProfileScreenFromUserResponse(response)
                        populateProfileFields()
                    }

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

    fun updateUserInfo(){
        if(!attemptUpdateUserInfo()) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val patch = UserPatch(
                    //name = _username.value,
                    email = _email.value,
                    //password = _password.value,
                    profile = ProfilePatch(
                        telephone = _phone.value,
                        address = _address.value,
                        photo = _photo.value
                    )
                )

                userRepository.updateCurrentUser(patch)

                withContext(Dispatchers.Main) {

                    _username.value?.let { newUsername ->
                        sharedPreferences.edit { putString("username", newUsername) }
                        loadUser()
                    }
                }

            } catch (e: Exception) {
                Log.e("UserViewModel", "Error actualizando usuario: ${e.message}")
            }
        }
    }

    fun attemptUpdateUserInfo(): Boolean{
        var isValid = true
        Log.d("UserViewModel", "Attempting to update user info: isValid: $isValid")
        _username.value?.let {
            if (_username.value!!.length > 50){
                _userNameError.postValue("El nombre de usuario no puede superar los 50 caracteres")
                isValid = false
            }
            Log.d("UserViewModel", "Username error: ${_userNameError.value}, isValid: $isValid")
        }

        _email.value?.let {
            if (!_email.value!!.contains("@") || !_email.value!!.contains(".")) {
                _emailError.postValue("El formato del email es inválido")
                isValid = false
                Log.d("UserViewModel", "Email error: ${_emailError.value}, isValid: $isValid")
            }
        }

        _phone.value?.let {
            val phonePattern = Regex("^[0-9]*$")
            if (!phonePattern.matches(_phone.value.toString())) {
                _phoneError.postValue("El teléfono solo puede contener números (sin espacios ni otros carácteres)")
                isValid = false
            }
            Log.d("UserViewModel", "Phone error: ${_phoneError.value}, isValid: $isValid")
        }

        address.value?.let {
            if(_address.value!!.length > 50){
                _addressError.postValue("La dirección no puede superar los 50 caracteres")
                isValid = false
            }
            Log.d("UserViewModel", "Address error: ${_addressError.value}, isValid: $isValid")
        }

        Log.d("UserViewModel", "isValid: $isValid")

        return isValid
    }

    fun populateProfileFields() {
        _username.postValue(_userProfile.value?.name)
        _email.postValue(_userProfile.value?.email)
        _phone.postValue(_userProfile.value?.profile?.telephone)
        _address.postValue(_userProfile.value?.profile?.address)
        _photo.postValue(_userProfile.value?.profile?.photo)
    }
}