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
    private val userRepository: UserRepository
) : ViewModel() {
    //Form create account info fields
    private var _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name
    fun onAccountNameChange(accountName: String) {
        _name.postValue(accountName)
    }

    private var _nameError = MutableLiveData<String?>(null)
    val nameError: LiveData<String?> get() = _nameError

    private var _moneyGoal = MutableLiveData<Double?>(null)
    val moneyGoal: LiveData<Double?> get() = _moneyGoal
    fun onAccountMoneyGoalChange(accountMoneyGoal: Double) {
        _moneyGoal.postValue(accountMoneyGoal)
    }

    private var _moneyGoalError = MutableLiveData<String?>(null)
    val moneyGoalError: LiveData<String?> get() = _moneyGoalError

    private var _dateGoal = MutableLiveData<LocalDate?>()
    val dateGoal: LiveData<LocalDate?> get() = _dateGoal
    fun onAccountDateGoalChange(accountDateGoal: LocalDate?) {
        accountDateGoal?.let {
            _dateGoal.postValue(it)
        } ?: run {
            _dateGoal.postValue(null)
        }
    }

    private var _dateGoalError = MutableLiveData<String?>(null)
    val dateGoalError: LiveData<String?> get() = _dateGoalError

    private var _photo = MutableLiveData<String>()
    val photo: LiveData<String> get() = _photo
    fun onAccountPhotoChange(accountPhoto: String) {
        _photo.postValue(accountPhoto)
    }

    private var _photoError = MutableLiveData<String?>(null)
    val photoError: LiveData<String?> get() = _photoError

    //Flag to check if the creation of an account is valid
    private val _isValidAccountCreate = MutableLiveData<Boolean>(false)
    val isValidAccountCreate: LiveData<Boolean> get() = _isValidAccountCreate

    //Flag to check if the creation of a contribution is valid
    private val _isValidContributionCreate = MutableLiveData<Boolean>(false)
    val isValidContributionCreate: LiveData<Boolean> get() = _isValidContributionCreate

    //Form new contribution info fields
    private var _contributionAmount = MutableLiveData<Double?>(null)
    val contributionAmount: LiveData<Double?> get() = _contributionAmount
    fun onContributionAmountChange(contributionAmount: Double) {
        _contributionAmount.postValue(contributionAmount)
    }
    private var _contributionAmountError = MutableLiveData<String?>(null)
    val contributionAmountError: LiveData<String?> get() = _contributionAmountError

    //Last created account
    private var _lastCreatedAccount = MutableLiveData<AccountOverview?>()
    val lastCreatedAccount: LiveData<AccountOverview?> get() = _lastCreatedAccount

    //current account
    private var _currentAccount = MutableLiveData<AccountDetails?>()
    val currentAccount: LiveData<AccountDetails?> get() = _currentAccount
    private var _savingsList = MutableLiveData<List<Saving>>()
    val savingsList: LiveData<List<Saving>> get() = _savingsList
    private var _currentAccountAmount = MutableLiveData<Double>(0.0)
    val currentAccountAmount: LiveData<Double> get() = _currentAccountAmount

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
                withContext(Dispatchers.Main) {
                    _currentAccount.value = mappedAccount
                    _currentAccountAmount.value = _currentAccount.value?.currentAmount
                }
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
                if(!attemptAccountCreation())return@launch
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
                if(!attemptContributionCreation())return@launch
                _contributionAmount.value?.let {
                    val newSaving = SavingCreate(
                        userRepository.getCurrentUserId(),
                        _contributionAmount.value!!.toDouble()
                    )
                    val newContribution = accountRepository.createNewContribution(
                        _currentAccount.value!!.id,
                        newSaving
                    )
                    _currentAccountAmount.postValue(newContribution.currentMoney)
                    val updatedList = listOf(newContribution) + _savingsList.value.orEmpty()
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

    fun loadingSavingsList() {
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
        _contributionAmount.postValue(null)
    }

    fun clearContributionError() {
            _contributionAmountError.postValue(null)
    }

    fun clearErrors(){
        _nameError.postValue(null)
        _moneyGoalError.postValue(null)
        _dateGoalError.postValue(null)
        _photoError.postValue(null)
    }

    suspend fun attemptContributionCreation(): Boolean {
        clearContributionError()

        var isValid = true

        if (_contributionAmount.value == null) {
            _contributionAmountError.postValue("Campo obligatorio")
            isValid = false
        } else if (_contributionAmount.value!! <= 0) {
            _contributionAmountError.postValue("La cantidad debe ser mayor a 0")
            isValid = false
        }

        withContext(Dispatchers.Main) { _isValidContributionCreate.value = isValid }

        return isValid
    }

    fun attemptAccountCreation(): Boolean {
        clearErrors()

        var isValid = true

        if (_name.value.isNullOrBlank()) {
            _nameError.postValue("Nombre de cuenta obligatorio")
            isValid = false
        } else if (_name.value!!.length > 100) {
            _nameError.postValue("Debe tener menos de 100 caracteres")
            isValid = false
        }

        if (_moneyGoal.value == null) {
            _moneyGoalError.postValue("Campo obligatorio")
            isValid = false
        } else if (_moneyGoal.value!! <= 0) {
            _moneyGoalError.postValue("La cantidad debe ser mayor a 0")
            isValid = false
        }

        if (_dateGoal.value == null) {
            _dateGoalError.postValue("Campo obligatorio")
            isValid = false
        } else if (!_dateGoal.value!!.isAfter(LocalDate.now())) {
            _dateGoalError.postValue("La fecha ha de ser posterior a hoy")
            isValid = false
        }

        // TODO: Validación de foto
        _isValidAccountCreate.postValue(isValid)
        return isValid
    }
}