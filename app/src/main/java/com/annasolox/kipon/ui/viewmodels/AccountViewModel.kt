package com.annasolox.kipon.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasolox.kipon.core.navigation.AccountNavigationEvent
import com.annasolox.kipon.core.utils.mappers.AccountMapper
import com.annasolox.kipon.data.repository.AccountRepository
import com.annasolox.kipon.ui.models.AccountDetails
import com.annasolox.kipon.ui.models.LoginUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class AccountViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {
    //Variables formulario de creación de hucha
    private var _name = MutableLiveData<String>("")
    val name: LiveData<String> get() = _name
    private var _moneyGoal = MutableLiveData<Double>(0.0)
    val moneyGoal: LiveData<Double> get() = _moneyGoal
    private var _dateGoal = MutableLiveData<LocalDate>(null)
    val dateGoal: LiveData<LocalDate?> get() = _dateGoal
    private var _photo = MutableLiveData<String>("")
    val photo: LiveData<String> get() = _photo

    //current account
    private var _currentAccount = MutableLiveData<AccountDetails?>()
    val currentAccount: LiveData<AccountDetails?> get() = _currentAccount

    //Navigation events
    private val _navigationEvent = MutableLiveData<AccountNavigationEvent?>()
    val navigationEvent: LiveData<AccountNavigationEvent?> get() = _navigationEvent

    //Loading state
    private var _loadingState = MutableLiveData<LoginUiState>(LoginUiState.Idle)
    val loadingState: LiveData<LoginUiState> get() = _loadingState

    fun loadCurrentAccount(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.postValue(LoginUiState.Loading)
            try {
                val accountResponse = accountRepository.getAccountById(id)
                Log.d("AccountViewModel", "Account response: $accountResponse")

                _loadingState.postValue(LoginUiState.Success("Account loaded successfully"))
                val mappedAccount = AccountMapper.toDetailAccount(accountResponse)
                withContext(Dispatchers.Main) { _currentAccount.value = mappedAccount  }
                Log.d("AccountViewModel", "Current account: ${_currentAccount.value}")

            } catch (e: Exception) {
                _loadingState.postValue(LoginUiState.Error("Error loading account"))
                Log.e("AccountViewModel", "Error loading account: ${e.message}")
            }
        }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    fun clearCurrentAccount() {
        _currentAccount.value = null
    }

}