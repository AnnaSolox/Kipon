package com.annasolox.kipon.ui.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasolox.kipon.core.navigation.LoginNavigationEvent
import com.annasolox.kipon.core.utils.mappers.UserMapper
import com.annasolox.kipon.data.api.models.request.create.LoginRequest
import com.annasolox.kipon.data.repository.AuthRepository
import com.annasolox.kipon.ui.models.LoginUiState
import com.annasolox.kipon.ui.screens.LoginScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sharedPreferences: SharedPreferences
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
    val navigationEvent: LiveData<LoginNavigationEvent?> get() = _navigationEvent

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

    private val mutex = Mutex()

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            _loginState.postValue(LoginUiState.Loading)
            try {
                if(!attemptLogin())return@launch

                val result = authRepository.login(LoginRequest(_userName.value!!, _password.value!!))
                val token = result.getOrThrow()

                Log.d("AuthViewModel", "Token: $token")

                if(token != "Nombre de usuario o contraseña incorrectos" && token.isNotEmpty()){
                    _loginState.postValue(LoginUiState.Success(token))
                    saveToken(token)
                    saveUserName(_userName.value!!)
                    _navigationEvent.postValue(LoginNavigationEvent.NavigateToHome)
                    Log.d("AuthViewModel", "Login successfull!")
                } else {
                    clearToken()
                    _loginState.postValue(LoginUiState.Error("Error al iniciar sesión"))
                    Log.d("AuthViewModel", "Error al iniciar sesión")
                }
            } catch (e: Exception) {
                _loginState.postValue(LoginUiState.Error("Error inesperado"))
                Log.e("AuthViewModel", "Error al iniciar sesión: ${e.message}")
            }
        }
    }

    fun register() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loginState.postValue(LoginUiState.Loading)
                if (!attemptRegister()) {
                    _loginState.postValue(LoginUiState.Idle)
                    return@launch
                }

                mutex.withLock {
                    val result = authRepository.register(
                        UserMapper.toUserCreate(
                            _userName.value!!,
                            _password.value!!,
                            _email.value!!,
                            _completeName.value!!,
                            _phone.value!!,
                            _address.value!!,
                            _photo.value
                        )
                    )

                    if (result.isSuccess) {
                        _loginState.postValue(LoginUiState.Success("User registered successfully"))
                        clearErrors()
                    } else {
                        val errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                        _loginState.postValue(LoginUiState.Error(errorMessage))
                        Log.d("AuthViewModel", "Error on registering user: $errorMessage")
                    }
                }

            } catch (e: Exception) {
                _loginState.postValue(LoginUiState.Error("Error inesperado: ${e.message}"))
                Log.e("AuthViewModel", "Error en registro", e)
            }
        }
    }

    private fun saveToken(token: String) {
        sharedPreferences.edit {
            putString("auth_token", token)
        }
    }

    private fun saveUserName(userName: String){
        sharedPreferences.edit {
            putString("username", userName)
        }
    }

    fun resetState() {
        _loginState.value = LoginUiState.Idle
    }

    fun attemptLogin(): Boolean {
        clearErrors()
        var isValid = true

        if (_userName.value.isNullOrBlank()) {
            _userNameError.value = "Nombre de usuario obligatorio"
            isValid = false
        } else if (_userName.value!!.length > 50) {
            _userNameError.value = "Debe tener menos de 50 caracteres"
            isValid = false
        }

        if (_password.value.isNullOrBlank()) {
            _passwordError.value = "Contraseña obligatoria"
            isValid = false
        } else if (_password.value!!.length < 8) {
            _passwordError.value = "Debe tener al menos 8 caracteres"
            isValid = false
        } else if (_password.value!!.length > 50) {
            _passwordError.value = "Debe tener menos de 50 caracteres"
            isValid = false
        }

        return isValid
    }

    fun attemptRegister(): Boolean{
        clearErrors()
        var isValid = true

        if (_userName.value.isNullOrBlank()) {
            _userNameError.postValue("Nombre de usuario obligatorio")
            isValid = false
        } else if (_userName.value!!.length > 50) {
            _userNameError.postValue("Debe tener menos de 50 caracteres")
            isValid = false
        }

        if (_password.value.isNullOrBlank()) {
            _passwordError.postValue("Contraseña obligatoria")
            isValid = false
        } else if (_password.value!!.length < 8) {
            _passwordError.postValue("Debe tener al menos 8 caracteres")
            isValid = false
        } else if (_password.value!!.length > 50) {
            _passwordError.postValue("Debe tener menos de 50 caracteres")
            isValid = false
        }

        if(_passwordConfirmation.value.isNullOrBlank() || _passwordConfirmation.value!! != _password.value) {
            _passwordConfirmationError.postValue("Las contraseñas no coinciden")
            isValid = false
        }

        if (_email.value.isNullOrBlank()) {
            _emailError.postValue("Correo electrónico obligatorio")
            isValid = false
        } else if (!_email.value!!.contains("@")) {
            _emailError.postValue("Correo electrónico no válido")
            isValid = false
        }

        if (_completeName.value.isNullOrBlank()) {
            _completeNameError.postValue("Nombre completo obligatorio")
            isValid = false
        } else if (_completeName.value!!.length > 100) {
            _completeNameError.postValue( "Debe tener menos de 100 caracteres")
            isValid = false
        }

        if (_phone.value.isNullOrBlank()) {
            _phoneError.postValue("Teléfono obligatorio")
            isValid = false

        } else if (!_phone.value!!.matches(Regex("[0-9]+")) || _phone.value!!.length != 9) {
            _phoneError.postValue("Teléfono no válido")
            isValid = false
        }

        if (_address.value.isNullOrBlank()) {
            _addressError.postValue("Dirección obligatoria")
            isValid = false
        } else if (_address.value!!.length > 50) {
            _addressError.postValue("Debe tener menos de 50 caracteres")
            isValid = false
        }

        return isValid
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
        sharedPreferences.edit { remove("auth_token") }
    }
}