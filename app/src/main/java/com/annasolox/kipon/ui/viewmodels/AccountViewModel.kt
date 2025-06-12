package com.annasolox.kipon.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annasolox.kipon.core.navigation.AccountNavigationEvent
import com.annasolox.kipon.core.utils.formatters.Formatters
import com.annasolox.kipon.data.api.models.request.create.AccountCreate
import com.annasolox.kipon.domain.account.AddUserToAccountUseCase
import com.annasolox.kipon.domain.account.CreateAccountUseCase
import com.annasolox.kipon.domain.account.CreateContributionUseCase
import com.annasolox.kipon.domain.account.GetAccountUseCase
import com.annasolox.kipon.domain.account.UpdateAccountUseCase
import com.annasolox.kipon.domain.image.UploadImageUseCase
import com.annasolox.kipon.ui.models.AccountDetails
import com.annasolox.kipon.ui.models.LoginUiState
import com.annasolox.kipon.ui.models.Saving
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class AccountViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
    private val createContributionUseCase: CreateContributionUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val addUserToAccountUseCase: AddUserToAccountUseCase
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

    private val _isImageUploaded = MutableLiveData<Boolean>(true)
    val isImageUploaded: LiveData<Boolean> get() = _isImageUploaded

    fun onAccountDateGoalChange(accountDateGoal: LocalDate?) {
        accountDateGoal?.let {
            _dateGoal.postValue(it)
        } ?: run {
            _dateGoal.postValue(null)
        }
    }

    private var _dateGoalError = MutableLiveData<String?>(null)
    val dateGoalError: LiveData<String?> get() = _dateGoalError

    private var _photo = MutableLiveData<String?>(null)
    val photo: LiveData<String?> get() = _photo
    fun onAccountPhotoChange(accountPhoto: String) {
        _photo.postValue(accountPhoto)
    }

    private var _photoError = MutableLiveData<String?>(null)

    //Flag to check if the creation of an account is valid
    private val _isValidAccountCreate = MutableLiveData<Boolean>(false)
    val isValidAccountCreate: LiveData<Boolean> get() = _isValidAccountCreate

    //Flag to check if the creation of a contribution is valid
    private val _isValidContributionCreate = MutableLiveData<Boolean>(false)
    val isValidContributionCreate: LiveData<Boolean> get() = _isValidContributionCreate

    //Flag to ckeck if the edit of an account is valid
    private val _isValidEditAccount = MutableLiveData<Boolean>(false)
    val isValidEditAccount: LiveData<Boolean> get() = _isValidEditAccount

    //Form new contribution info fields
    private var _contributionAmount = MutableLiveData<Double?>(null)
    val contributionAmount: LiveData<Double?> get() = _contributionAmount
    fun onContributionAmountChange(contributionAmount: Double) {
        _contributionAmount.postValue(contributionAmount)
    }

    private var _contributionAmountError = MutableLiveData<String?>(null)
    val contributionAmountError: LiveData<String?> get() = _contributionAmountError


    //current account
    private var _currentAccount = MutableLiveData<AccountDetails?>()
    val currentAccount: LiveData<AccountDetails?> get() = _currentAccount
    private var _savingsList = MutableLiveData<List<Saving>>()
    val savingsList: LiveData<List<Saving>> get() = _savingsList
    private var _currentAccountAmount = MutableLiveData<Double>(0.0)
    val currentAccountAmount: LiveData<Double> get() = _currentAccountAmount

    //Account edit form fields
    private var _editAccountName = MutableLiveData<String>()
    val editAccountName: LiveData<String> get() = _editAccountName
    fun onEditAccountNameChange(accountName: String) {
        _editAccountName.postValue(accountName)
    }

    private var _editAccountNameError = MutableLiveData<String?>(null)
    val editAccountNameError: LiveData<String?> get() = _editAccountNameError

    private var _editAccountMoneyGoal = MutableLiveData<Double?>(null)
    val editAccountMoneyGoal: LiveData<Double?> get() = _editAccountMoneyGoal
    fun onEditAccountMoneyGoalChange(accountMoneyGoal: Double) {
        _editAccountMoneyGoal.postValue(accountMoneyGoal)
    }

    private var _editAccountMoneyGoalError = MutableLiveData<String?>(null)
    val editAccountMoneyGoalError: LiveData<String?> get() = _editAccountMoneyGoalError

    private var _editAccountDateGoal = MutableLiveData<LocalDate?>()
    val editAccountDateGoal: LiveData<LocalDate?> get() = _editAccountDateGoal
    fun onEditAccountDateGoalChange(accountDateGoal: LocalDate?) {
        accountDateGoal?.let {
            _editAccountDateGoal.postValue(it)
        } ?: run {
            _editAccountDateGoal.postValue(null)
        }
    }

    private var _editAccountDateGoalError = MutableLiveData<String?>(null)
    val editAccountDateGoalError: LiveData<String?> get() = _editAccountDateGoalError

    private var _editAccountPhoto = MutableLiveData<String>("")
    val editAccountPhoto: LiveData<String> get() = _editAccountPhoto
    fun onEditAccountPhotoChange(accountPhoto: String) {
        _editAccountPhoto.postValue(accountPhoto)
    }

    private var _editAccountPhotoError = MutableLiveData<String?>(null)


    //Navigation events
    private val _navigationEvent = MutableLiveData<AccountNavigationEvent?>()
    val navigationEvent: LiveData<AccountNavigationEvent?> get() = _navigationEvent

    //Loading state
    private var _loadingState = MutableLiveData<LoginUiState>(LoginUiState.Idle)

    private val _onUserAdded = MutableSharedFlow<Unit>()
    val onUserAdded = _onUserAdded.asSharedFlow()

    fun loadCurrentAccount(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.postValue(LoginUiState.Loading)
            try {
                val mappedAccount = getAccountUseCase(id)
                withContext(Dispatchers.Main) {
                    _currentAccount.value = mappedAccount
                    _currentAccountAmount.value = _currentAccount.value?.currentAmount
                    populateEditAccountForm()
                    _loadingState.value = LoginUiState.Success("Cuenta cargada con éxito")
                }

            } catch (e: Exception) {
                _loadingState.postValue(LoginUiState.Error("Error cargando la cuenta"))
                Log.e("AccountViewModel", "Error loading account: ${e.message}")
            }
        }
    }

    fun createNewAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            _isValidAccountCreate.postValue(false)
            _loadingState.postValue(LoginUiState.Loading)

            val accountCreate = AccountCreate(
                name = _name.value ?: "",
                moneyGoal = _moneyGoal.value?.toDouble() ?: 0.0,
                dateGoal = _dateGoal.value,
                photo = _photo.value
            )

            val result = createAccountUseCase(accountCreate)


            if (result.success && result.accountId != null) {
                loadCurrentAccount(result.accountId)
                withContext(Dispatchers.Main) {
                    _loadingState.value = LoginUiState.Success("Cuenta creada con éxito")
                    _isValidAccountCreate.value = true
                    _navigationEvent.value = AccountNavigationEvent.NavigateToAccountDetail
                }
            } else {
                withContext(Dispatchers.Main) {
                    _nameError.value = result.nameError
                    _moneyGoalError.value = result.moneyGoalError
                    _dateGoalError.value = result.dateGoalError
                    _loadingState.value =
                        LoginUiState.Error(
                            result.generalError ?: "Error validando la cuenta"
                        )
                }
            }
        }
    }

    fun createNewContribution() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.postValue(LoginUiState.Loading)
            val amount = _contributionAmount.value
            val result = createContributionUseCase(_currentAccount.value!!.id, amount)

            if (result.success) {
                val newContribution = result.newContribution!!
                _currentAccountAmount.postValue(newContribution.currentMoney)

                val updatedList = listOf(newContribution) + _savingsList.value.orEmpty()
                _savingsList.postValue(updatedList)

                _loadingState.postValue(LoginUiState.Success("Transacción creada con éxito"))
                clearContributionError()
                clearContributionForm()
                _isValidContributionCreate.postValue(true)
            } else {
                _contributionAmountError.postValue(result.amountError)
                _loadingState.postValue(
                    LoginUiState.Error(
                        result.generalError ?: "Error validando la creación de la transacción"
                    )
                )
                _isValidContributionCreate.postValue(false)
            }
        }
    }

    fun updateAccountInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = updateAccountUseCase(
                accountId = _currentAccount.value!!.id,
                name = _editAccountName.value,
                moneyGoal = _editAccountMoneyGoal.value?.toDouble(),
                dateGoal = _editAccountDateGoal.value,
                photo = _editAccountPhoto.value
            )

            if (result.success) {
                loadCurrentAccount(_currentAccount.value!!.id)
                withContext(Dispatchers.Main) {
                    populateEditAccountForm()
                    clearEditAccountError()
                    _isValidEditAccount.value = true
                }
            } else {
                withContext(Dispatchers.Main) {
                    _editAccountNameError.value = result.nameError
                    _moneyGoalError.value = result.moneyGoalError
                    Log.e(
                        "AccountViewModel",
                        "Error validando actualización: ${result.generalError}"
                    )
                    _isValidEditAccount.value = false
                }

            }
        }
    }

    fun addUserToAccount(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val accountId = _currentAccount.value?.id ?: return@launch

            val result = addUserToAccountUseCase(userId, accountId)
            if (result.success) {
                loadCurrentAccount(accountId)
                _onUserAdded.emit(Unit)
            } else {
                Log.d("AccountViewModel", "Error al añadir usuario: ${result.errorMessage}")
            }
        }
    }

    fun uploadImage(image: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            _isImageUploaded.postValue(false)
            val result = uploadImageUseCase(image, "account")
            withContext(Dispatchers.Main) {
                result.onSuccess { url ->
                    _editAccountPhoto.value = url
                    _photo.value = url
                }.onFailure { e ->
                    Log.e("UserViewModel", "Error uploading image: ${e.message}")
                }
                _isImageUploaded.postValue(true)
            }
        }
    }


    fun populateEditAccountForm() {
        _editAccountName.value = _currentAccount.value?.name
        _editAccountMoneyGoal.value = _currentAccount.value?.moneyGoal
        _editAccountDateGoal.value = Formatters.parseDate(_currentAccount.value!!.dateGoal)
        _editAccountPhoto.value = _currentAccount.value?.photo.toString()
    }

    fun clearEditAccountError() {
        _editAccountNameError.postValue(null)
        _editAccountMoneyGoalError.postValue(null)
        _editAccountMoneyGoalError.postValue(null)
        _editAccountPhotoError.postValue(null)
    }

    fun resetEditAccountValidation() {
        _isValidEditAccount.postValue(false)
    }

    fun resetAddContibutionValidation() {
        _isValidContributionCreate.postValue(false)
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
        _name.value = ""
        _moneyGoal.value = null
        _dateGoal.value = null
        _photo.value = ""
    }

    fun clearContributionForm() {
        _contributionAmount.postValue(null)
    }

    fun clearContributionError() {
        _contributionAmountError.postValue(null)
    }

    fun clearErrors() {
        _nameError.postValue(null)
        _moneyGoalError.postValue(null)
        _dateGoalError.postValue(null)
        _photoError.postValue(null)
    }
}