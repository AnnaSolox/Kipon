package com.annasolox.kipon.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasolox.kipon.core.navigation.AccountNavigationEvent
import com.annasolox.kipon.core.utils.mappers.AccountMapper
import com.annasolox.kipon.data.api.models.request.create.AccountCreate
import com.annasolox.kipon.data.api.models.request.create.SavingCreate
import com.annasolox.kipon.data.repository.AccountRepository
import com.annasolox.kipon.data.repository.UserRepository
import com.annasolox.kipon.ui.models.AccountDetails
import com.annasolox.kipon.ui.models.AccountOverview
import com.annasolox.kipon.ui.models.LoginUiState
import com.annasolox.kipon.ui.models.Saving
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class AccountViewModel(
    private val accountRepository: AccountRepository,
    private val userReository: UserRepository
) : ViewModel() {
    //Form create account info fields
    private var _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name
    fun onAccountNameChange(accountName: String) {
        _name.postValue(accountName)
    }

    private var _moneyGoal = MutableLiveData<Double?>(null)
    val moneyGoal: LiveData<Double?> get() = _moneyGoal
    fun onAccountMoneyGoalChange(accountMoneyGoal: Double) {
        _moneyGoal.postValue(accountMoneyGoal)
    }

    private var _dateGoal = MutableLiveData<LocalDate?>()
    val dateGoal: LiveData<LocalDate?> get() = _dateGoal
    fun onAccountDateGoalChange(accountDateGoal: LocalDate?) {
        accountDateGoal?.let {
            _dateGoal.postValue(it)
        } ?: run {
            _dateGoal.postValue(null)
        }
    }

    private var _photo = MutableLiveData<String>()
    val photo: LiveData<String> get() = _photo
    fun onAccountPhotoChange(accountPhoto: String) {
        _photo.postValue(accountPhoto)
    }

    //Form new contribution info fields
    private var _contributionAmount = MutableLiveData<Double>(0.0)
    val contributionAmount: LiveData<Double> get() = _contributionAmount
    fun onContributionAmountChange(contributionAmount: Double) {
        _contributionAmount.postValue(contributionAmount)
    }

    //Last created account
    private var _lastCreatedAccount = MutableLiveData<AccountOverview?>()
    val lastCreatedAccount: LiveData<AccountOverview?> get() = _lastCreatedAccount

    //current account
    private var _currentAccount = MutableLiveData<AccountDetails?>()
    val currentAccount: LiveData<AccountDetails?> get() = _currentAccount
    private var _savingsList = MutableLiveData<List<Saving>>()
    val savingsList: LiveData<List<Saving>> get() = _savingsList

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
                withContext(Dispatchers.Main) { _currentAccount.value = mappedAccount }
                Log.d("AccountViewModel", "Current account: ${_currentAccount.value}")

            } catch (e: Exception) {
                _loadingState.postValue(LoginUiState.Error("Error loading account"))
                Log.e("AccountViewModel", "Error loading account: ${e.message}")
            }
        }
    }

    fun createNewAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.postValue(LoginUiState.Loading)
            try {
                val accountToCreate = AccountCreate(
                    name = _name.value.toString(),
                    moneyGoal = _moneyGoal.value!!.toDouble(),
                    dateGoal = _dateGoal.value,
                    photo = _photo.value.toString()
                )
                val createdAccount = accountRepository.accountCreate(accountToCreate)
                _lastCreatedAccount.postValue(createdAccount)
                loadCurrentAccount(createdAccount.id)
                _navigationEvent.postValue(AccountNavigationEvent.NavigateToAccountDetail)

                _loadingState.postValue(LoginUiState.Success("Account created successfully"))
            } catch (e: Exception) {
                _loadingState.postValue(LoginUiState.Error("Error creating account"))
                Log.e("AccountViewModel", "Error creating account: ${e.message}")
            }
        }
    }

    fun createNewContribution() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.postValue(LoginUiState.Loading)
            try {
                _contributionAmount.value?.let {
                    val newSaving = SavingCreate(
                        userReository.getCurrentUserId(),
                        _contributionAmount.value!!.toDouble()
                    )
                    val newContribution = accountRepository.createNewContribution(
                        _currentAccount.value!!.id,
                        newSaving
                    )
                    val updatedList = _savingsList.value.orEmpty() + newContribution
                    val savingList = updatedList.map { it }
                    _savingsList.postValue(savingList)

                    _loadingState.postValue(LoginUiState.Success("Contribution created successfully"))
                    Log.d("AccountViewModel", "New contribution: $newContribution")
                }
            } catch (e: Exception) {
                _loadingState.postValue(LoginUiState.Error("Error creating contribution"))
                Log.d("AccountViewModel", "Error creating contribution: ${e.message}")
            }
        }
    }

    fun loadingSavingsList(){
        viewModelScope.launch(Dispatchers.IO) {
            val savings = _currentAccount.value!!.savings
            _savingsList.postValue(savings)
        }
    }


    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    fun clearCurrentAccount() {
        _currentAccount.value = null
    }

    fun clearCreateForm() {
        _name.postValue("")
        _moneyGoal.postValue(null)
        _dateGoal.postValue(null)
        _photo.postValue("")
    }

    fun clearContributionForm() {
        _contributionAmount.postValue(0.0)
    }
}