package com.annasolox.kipon.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasolox.kipon.core.navigation.LoginNavigationEvent
import com.annasolox.kipon.domain.auth.ClearTokenUseCase
import com.annasolox.kipon.domain.auth.LoginUseCase
import com.annasolox.kipon.domain.auth.RegisterUseCase
import com.annasolox.kipon.domain.auth.SaveTokenUseCase
import com.annasolox.kipon.domain.auth.SaveUserNameUSeCase
import com.annasolox.kipon.ui.models.LoginUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val saveUserNameUSeCase: SaveUserNameUSeCase,
    private val clearTokenUseCase: ClearTokenUseCase
) : ViewModel() {

    private var _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName
    private val _userNameError = MutableLiveData<String?>(null)
    val userNameError: LiveData<String?> get() = _userNameError
    private var _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password
    private var _passwordConfirmation = MutableLiveData<String>()
    val passwordConfirmation: LiveData<String> get() = _passwordConfirmation
    private var _passwordConfirmationError = MutableLiveData<String?>(null)
    val passwordConfirmationError: LiveData<String?> get() = _passwordConfirmationError
    private var _passwordError = MutableLiveData<String?>(null)
    val passwordError: LiveData<String?> get() = _passwordError
    private var _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email
    private var _emailError = MutableLiveData<String?>(null)
    val emailError: LiveData<String?> get() = _emailError
    private var _completeName = MutableLiveData<String>()
    val completeName: LiveData<String> get() = _completeName
    private var _completeNameError = MutableLiveData<String?>(null)
    val completeNameError: LiveData<String?> get() = _completeNameError
    private var _phone = MutableLiveData<String>()
    val phone: LiveData<String> get() = _phone
    private var _phoneError = MutableLiveData<String?>(null)
    val phoneError: LiveData<String?> get() = _phoneError
    private var _address = MutableLiveData<String>()
    val address: LiveData<String> get() = _address
    private var _addressError = MutableLiveData<String?>(null)
    val addressError: LiveData<String?> get() = _addressError
    private var _photo = MutableLiveData<String>()

    private var _loginState = MutableLiveData<LoginUiState>(LoginUiState.Idle)
    val loginState: LiveData<LoginUiState> get() = _loginState

    //Navigation events
    private val _navigationEvent = MutableLiveData<LoginNavigationEvent?>()

    fun onUserNameChanged(userName: String) {
        _userName.postValue(userName)
    }

    fun onPasswordChanged(password: String) {
        _password.postValue(password)
    }

    fun onPasswordConfirmationChanged(passwordConfirmation: String) {
        _passwordConfirmation.postValue(passwordConfirmation)
    }

    fun onEmailChanged(email: String) {
        _email.postValue(email)
    }

    fun onCompleteNameChanged(completeName: String) {
        _completeName.postValue(completeName)
    }

    fun onPhoneChanged(phone: String) {
        _phone.postValue(phone)
    }

    fun onAddressChanged(address: String) {
        _address.postValue(address)
    }

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            _loginState.postValue(LoginUiState.Loading)

            val result = loginUseCase(_userName.value ?: "", _password.value ?: "")

            if (!result.success) {
                _userNameError.postValue(result.usernameError)
                _passwordError.postValue(result.passwordError)

                if (result.usernameError == null && result.passwordError == null) {
                    _loginState.postValue(LoginUiState.Error("Error al iniciar sesión"))
                }

                return@launch
            }

            val token = result.token
            if (token != null && token.isNotEmpty()) {
                _loginState.postValue(LoginUiState.Success(token))
                saveToken(token)
                saveUserName(_userName.value!!)
                _navigationEvent.postValue(LoginNavigationEvent.NavigateToHome)
                Log.d("AuthViewModel", "Login successful!")
            } else {
                clearToken()
                _loginState.postValue(LoginUiState.Error("Error al iniciar sesión"))
                Log.d("AuthViewModel", "Error al iniciar sesión")
            }
        }
    }

    fun register() {
        viewModelScope.launch(Dispatchers.IO) {
            _loginState.postValue(LoginUiState.Loading)

            val result = registerUseCase(
                _userName.value,
                _password.value,
                _passwordConfirmation.value,
                _email.value,
                _completeName.value,
                _phone.value,
                _address.value,
                _photo.value
            )

            if (!result.success) {
                _userNameError.postValue(result.usernameError)
                _passwordError.postValue(result.passwordError)
                _passwordConfirmationError.postValue(result.passwordConfirmationError)
                _emailError.postValue(result.emailError)
                _completeNameError.postValue(result.completeNameError)
                _phoneError.postValue(result.phoneError)
                _addressError.postValue(result.addressError)

                if (result.message != null) {
                    _loginState.postValue(LoginUiState.Error(result.message))
                } else {
                    _loginState.postValue(LoginUiState.Idle)
                }

                return@launch
            }

            clearErrors()
            _loginState.postValue(LoginUiState.Success("Usuario registrado con éxito"))
        }
    }

    private fun saveToken(token: String) {
        saveTokenUseCase(token)
    }

    private fun saveUserName(userName: String) {
        saveUserNameUSeCase(userName)
    }

    fun clearErrors() {
        _userNameError.postValue(null)
        _passwordError.postValue(null)
        _passwordConfirmationError.postValue(null)
        _emailError.postValue(null)
        _completeNameError.postValue(null)
        _phoneError.postValue(null)
        _addressError.postValue(null)
    }

    fun clearToken() {
        clearTokenUseCase()
    }
}