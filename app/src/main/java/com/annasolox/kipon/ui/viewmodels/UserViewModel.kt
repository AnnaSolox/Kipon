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
import com.annasolox.kipon.data.api.models.response.UserResponse
import com.annasolox.kipon.domain.image.UploadImageUseCase
import com.annasolox.kipon.domain.user.GetUserUseCase
import com.annasolox.kipon.domain.user.SearchUserUseCase
import com.annasolox.kipon.domain.user.UpdateUserUseCase
import com.annasolox.kipon.ui.models.Saving
import com.annasolox.kipon.ui.models.SearchedUser
import com.annasolox.kipon.ui.models.UserHomeScreen
import com.annasolox.kipon.ui.models.UserProfileScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    private val sharedPreferences: SharedPreferences,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val searchUserUseCase: SearchUserUseCase
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

    private val _fetchedUsers = MutableLiveData<List<SearchedUser>>()
    val fetchedUsers: LiveData<List<SearchedUser>> get() = _fetchedUsers
    private val _fetchedUsersError = MutableLiveData<String?>(null)
    val fetchedUsersError: LiveData<String?> get() = _fetchedUsersError

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _isImageUploaded = MutableLiveData<Boolean>(true)
    val isImageUploaded: LiveData<Boolean> get() = _isImageUploaded

    fun searchUsers(partialName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _fetchedUsersError.postValue(null)
                _loading.postValue(true)
                _fetchedUsers.postValue(emptyList())
                val users = searchUserUseCase(partialName)

                _fetchedUsers.postValue(users)

                _loading.postValue((false))
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loading.postValue((false))
                    _fetchedUsersError.value = "No se han encontrado usuarios."
                }
                Log.e("UserViewModel", "Error buscando usuarios: ${e.message}")
            }

        }
    }

    fun loadUser() {
        viewModelScope.launch {
            try {
                val result = getUserUseCase()
                result.onSuccess { data ->
                    withContext(Dispatchers.Main) {
                        _userHome.value = data.userHome
                        _userProfile.value = data.userProfile
                        populateProfileFields()
                    }
                }.onFailure { e ->
                    Log.e("UserViewModel", "Error cargando usuario: ${e.message}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error inesperado: ${e.message}")
            }
        }
    }

    fun getSavingsFromUser() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("UserViewModel", "Userhome: ${_userHome.value}")
            Log.d("UserViewModel", "Obteniendo contribuciones del usuario")
            _userHome.value?.savings?.let {
                val savings = _userHome.value?.savings
                savings?.let { _allUserSavings.postValue(savings) }
                Log.d("UserViewModel", "Savings: $savings")
            }
        }
    }

    fun updateUserInfo() {
        viewModelScope.launch {
            val result = updateUserUseCase(
                email = _email.value,
                phone = _phone.value,
                address = _address.value,
                photo = _photo.value
            )

            _emailError.value = result.emailError
            _phoneError.value = result.phoneError
            _addressError.value = result.addressError

            if (!result.success) return@launch

            _username.value?.let { newUsername ->
                sharedPreferences.edit { putString("username", newUsername) }
                loadUser()
            }
        }

    }

    fun uploadImage(image: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            _isImageUploaded.postValue(false)
            val result = uploadImageUseCase(image, "profile")
            withContext(Dispatchers.Main) {
                result.onSuccess { url ->
                    _photo.value = url
                }.onFailure { e ->
                    Log.e("UserViewModel", "Error uploading image: ${e.message}")
                }
                _isImageUploaded.postValue(true)
            }
        }
    }

    fun clearFetchedUsers() {
        _fetchedUsers.postValue(emptyList())
    }

    fun setUserProfile(userResponse: UserResponse) {
        val userToProfile = toUserProfileScreenFromUserResponse(userResponse)
        _userProfile.postValue(userToProfile)
    }

    fun setUserHome(userResponse: UserResponse) {
        val userToHome = toUserHomeScreen(userResponse)
        _userHome.postValue(userToHome)
    }

    fun populateProfileFields() {
        _username.postValue(_userProfile.value?.name)
        _email.postValue(_userProfile.value?.email)
        _phone.postValue(_userProfile.value?.profile?.telephone)
        _address.postValue(_userProfile.value?.profile?.address)
        _photo.postValue(_userProfile.value?.profile?.photo)
    }
}