package com.annasolox.kipon

import android.content.SharedPreferences
import android.util.LiveDataTestUtils.getOrAwaitNonEmptyValue
import android.util.LiveDataTestUtils.getOrAwaitValue
import android.util.MainDispatcherRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.annasolox.kipon.data.api.models.response.AccountResponse
import com.annasolox.kipon.data.api.models.response.AccountRoleResponse
import com.annasolox.kipon.data.api.models.response.SavingResponse
import com.annasolox.kipon.data.api.models.response.SimplifiedAccountResponse
import com.annasolox.kipon.data.api.models.response.UserProfileResponse
import com.annasolox.kipon.data.api.models.response.UserResponse
import com.annasolox.kipon.data.api.models.response.UserRolResponse
import com.annasolox.kipon.data.api.models.response.UserSimplified
import com.annasolox.kipon.domain.account.AddUserToAccountUseCase
import com.annasolox.kipon.domain.account.CreateAccountUseCase
import com.annasolox.kipon.domain.account.CreateContributionUseCase
import com.annasolox.kipon.domain.account.GetAccountUseCase
import com.annasolox.kipon.domain.account.UpdateAccountUseCase
import com.annasolox.kipon.domain.image.UploadImageUseCase
import com.annasolox.kipon.domain.user.GetUserResult
import com.annasolox.kipon.domain.user.GetUserUseCase
import com.annasolox.kipon.domain.user.SearchUserUseCase
import com.annasolox.kipon.domain.user.UpdateUserUseCase
import com.annasolox.kipon.ui.models.AccountDetails
import com.annasolox.kipon.ui.models.AccountOverview
import com.annasolox.kipon.ui.models.Profile
import com.annasolox.kipon.ui.models.Saving
import com.annasolox.kipon.ui.models.SearchedUser
import com.annasolox.kipon.ui.models.UserHomeScreen
import com.annasolox.kipon.ui.models.UserProfileScreen
import com.annasolox.kipon.ui.viewmodels.AccountViewModel
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class UserDataUnitTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
    private lateinit var viewModel: UserViewModel
    private lateinit var accountViewModel: AccountViewModel

    lateinit var userResponseMock: UserResponse
    lateinit var accountsOverviewMock: List<AccountOverview>
    lateinit var savingsMock: List<Saving>
    lateinit var userHomeScreenMock: UserHomeScreen
    lateinit var userProfileScreen: UserProfileScreen
    lateinit var mockAccount: AccountDetails
    lateinit var accountResponseMock: AccountResponse

    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var uploadImageUseCase: UploadImageUseCase
    private lateinit var updateUserUseCase: UpdateUserUseCase
    private lateinit var searchUserUseCase: SearchUserUseCase
    private lateinit var createAccountUseCase: CreateAccountUseCase
    private lateinit var addUserToAccountUseCase: AddUserToAccountUseCase
    private lateinit var createContributionUseCase: CreateContributionUseCase
    private lateinit var getAccountUseCase: GetAccountUseCase
    private lateinit var updateAccountUseCase: UpdateAccountUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatcherRule.testDispatcher)
        getUserUseCase = mockk<GetUserUseCase>()
        uploadImageUseCase = mockk<UploadImageUseCase>()
        updateUserUseCase = mockk<UpdateUserUseCase>()
        searchUserUseCase = mockk<SearchUserUseCase>()
        createAccountUseCase = mockk<CreateAccountUseCase>()
        addUserToAccountUseCase = mockk<AddUserToAccountUseCase>()
        createContributionUseCase = mockk<CreateContributionUseCase>()
        getAccountUseCase = mockk<GetAccountUseCase>()
        updateAccountUseCase = mockk<UpdateAccountUseCase>()
        viewModel = UserViewModel(
            sharedPreferences = sharedPreferences,
            getUserUseCase = getUserUseCase,
            updateUserUseCase = updateUserUseCase,
            uploadImageUseCase = uploadImageUseCase,
            searchUserUseCase = searchUserUseCase
        )
        accountViewModel = AccountViewModel(
            uploadImageUseCase = uploadImageUseCase,
            getAccountUseCase = getAccountUseCase,
            createAccountUseCase = createAccountUseCase,
            createContributionUseCase = createContributionUseCase,
            updateAccountUseCase = updateAccountUseCase,
            addUserToAccountUseCase = addUserToAccountUseCase
        )

        userResponseMock = UserResponse(
            id = 1,
            name = "Ana",
            email = "anna@mail.com",
            registerDate = LocalDate.parse("2023-10-26"),
            profile = UserProfileResponse(
                id = 1,
                completeName = "Ana Soler",
                telephone = "123456789",
                address = "Calle Ejemplo 123",
                photo = "https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg"
            ),
            accountRole = listOf(
                AccountRoleResponse(
                    account = SimplifiedAccountResponse(
                        id = 1,
                        name = "Cuenta Ahorro",
                        dateGoal = LocalDate.parse("2032-10-12"),
                        currentMoney = 5000.0,
                        moneyGoal = 10000.0,
                        photo = ""
                    ),
                    role = "Administrador",
                )
            ),
            savings = listOf(
                SavingResponse(
                    id = 1,
                    userName = "Ana",
                    accountName = "Ahorro Ana",
                    userId = 1,
                    amount = 1000.0,
                    date = LocalDate.parse("2030-10-26"),
                    userPhoto = "https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg",
                    accountPhoto = null,
                    currentMoney = 500.0,
                ),
                SavingResponse(
                    id = 2,
                    userId = 1,
                    userName = "Ana",
                    accountName = "Ahorro Ana 2",
                    amount = 2000.0,
                    currentMoney = 1500.0,
                    date = LocalDate.parse("2030-10-27"),
                    userPhoto = "https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg",
                    accountPhoto = null
                )
            ),
        )

        accountsOverviewMock = listOf(
            AccountOverview(
                id = 1,
                name = "Cuenta Ahorro",
                dateGoal = "12/10/2032",
                currentMoney = 5000.0,
                moneyGoal = 10000.0,
                photo = ""
            )
        )

        savingsMock = listOf(
            Saving(
                id = 1,
                userId = 1,
                username = "Ana",
                accountName = "Ahorro Ana",
                amount = 1000.0,
                currentMoney = 500.0,
                date = "26/10/2030",
                userPhoto = "https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg",
                accountPhoto = ""
            ),
            Saving(
                id = 2,
                userId = 1,
                username = "Ana",
                accountName = "Ahorro Ana 2",
                amount = 2000.0,
                currentMoney = 1500.0,
                date = "27/10/2030",
                userPhoto = "https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg",
                accountPhoto = ""
            )
        )

        userHomeScreenMock = UserHomeScreen(
            userName = "Ana",
            photoUrl = "https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg",
            savings = savingsMock,
            accounts = accountsOverviewMock
        )

        userProfileScreen = UserProfileScreen(
            id = 1,
            name = "Ana",
            email = "anna@mail.com",
            profile = Profile(
                id = 1,
                completeName = "Ana Soler",
                telephone = "123456789",
                address = "Calle Ejemplo 123",
                photo = "https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg"
            ),
            registrationDate = LocalDate.parse("2023-10-26")
        )

        mockAccount = AccountDetails(
            id = 1L,
            name = "Cuenta Ahorro",
            photo = "",
            currentAmount = 500.0,
            moneyGoal = 500000.0,
            dateGoal = "10/12/2032",
            userMembers = listOf(
                UserSimplified(
                    id = 1L,
                    name = "Ana",
                    photo = "https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg"
                )
            ),
            admin = "Ana",
            savings = savingsMock,
        )

        accountResponseMock = AccountResponse(
            id = 1L,
            name = "Cuenta Ahorro",
            photo = "",
            currentMoney = 500.0,
            moneyGoal = 500000.0,
            creationDate = LocalDateTime.of(2032, 12, 10, 0, 0),
            goalDate = LocalDate.parse("2032-12-10"),
            userMembers = listOf(
                UserRolResponse(
                    user = UserSimplified(
                        id = 1L,
                        name = "Ana",
                        photo = "https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg"
                    ),
                    role = "Aministrador"
                )
            ),
            admin = "Ana",
            savings = arrayListOf(
                SavingResponse(
                    id = 1,
                    userId = 1,
                    userName = "Ana",
                    accountName = "Ahorro Ana",
                    amount = 1000.0,
                    currentMoney = 500.0,
                    date = LocalDate.of(2030, 10, 26),
                    userPhoto = "https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg",
                    accountPhoto = ""
                ),
                SavingResponse(
                    id = 2,
                    userId = 1,
                    userName = "Ana",
                    accountName = "Ahorro Ana 2",
                    amount = 2000.0,
                    currentMoney = 1500.0,
                    date = LocalDate.of(2030, 10, 27),
                    userPhoto = "https://kipon-images.s3.us-east-1.amazonaws.com/profiles/1747805991058_image.jpg",
                    accountPhoto = ""
                )
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun informacionActualizarUsuarioExito() = runTest {
        viewModel.onEmailChanged("usuario@ejemplo.com")
        viewModel.onPhoneChanged("123456789")
        viewModel.onAddressChanged("Calle Ejemplo 123")

        coEvery {
            updateUserUseCase.invoke(
                email = "usuario@ejemplo.com",
                phone = "123456789",
                address = "Calle Ejemplo 123",
                photo = any()
            )
        } returns UpdateUserUseCase.Result(success = true)

        viewModel.updateUserInfo()

        advanceUntilIdle()

        Assert.assertNull(viewModel.emailError.value)
        Assert.assertNull(viewModel.phoneError.value)
        Assert.assertNull(viewModel.addressError.value)
    }

    @Test
    fun informacionActualizarUsuarioFallo() = runTest {
        viewModel.onEmailChanged("usuarioejemplo.com") //Email inválido
        viewModel.onPhoneChanged("123456789")
        viewModel.onAddressChanged("Calle Ejemplo 123")

        coEvery {
            updateUserUseCase.invoke(
                email = "usuarioejemplo.com",
                phone = "123456789",
                address = "Calle Ejemplo 123",
                photo = any()
            )
        } returns UpdateUserUseCase.Result(
            success = false,
            emailError = "El formato del email es inválido"
        )

        viewModel.updateUserInfo()

        advanceUntilIdle()

        Assert.assertEquals("El formato del email es inválido", viewModel.emailError.value)
    }

    @Test
    fun busquedaUsuarioNombreParcialExito() = runTest {
        val nombreParcial = "Ana"
        val usuariosMock = listOf(
            SearchedUser(id = 1, userName = "Ana Pérez", photo = null),
            SearchedUser(id = 2, userName = "Anastasia Gómez", photo = null)
        )

        coEvery { searchUserUseCase.invoke(nombreParcial) } returns usuariosMock
        viewModel.searchUsers(nombreParcial)
        advanceUntilIdle()

        val result = viewModel.fetchedUsers.getOrAwaitNonEmptyValue()

        Assert.assertEquals(usuariosMock, result)
    }

    @Test
    fun cargaAhorrosUsuarioExito() = runTest {

        coEvery { sharedPreferences.getString("username", null) } returns "Ana"
        coEvery { getUserUseCase.invoke() } returns Result.success(
            GetUserResult(userHome = userHomeScreenMock, userProfile = userProfileScreen)
        )

        viewModel.loadUser()
        advanceUntilIdle()

        val userHome = viewModel.userHome.getOrAwaitValue()

        viewModel.getSavingsFromUser()
        advanceUntilIdle()

        val result = viewModel.allUserSavings.getOrAwaitValue()

        Assert.assertEquals(userHomeScreenMock, userHome)
        Assert.assertEquals(savingsMock, result)
    }

    @Test
    fun cargarFotoPerfilExito() = runTest {
        coEvery { sharedPreferences.getString("username", null) } returns "Ana"
        coEvery { getUserUseCase.invoke() } returns Result.success(
            GetUserResult(userHome = userHomeScreenMock, userProfile = userProfileScreen)
        )

        viewModel.loadUser()
        advanceUntilIdle()

        val userProfile = viewModel.userProfile.getOrAwaitValue()

        Assert.assertEquals(userProfileScreen.profile.photo, userProfile.profile.photo)
    }

    @Test
    fun cargarCuentasUsuarioExito() = runTest {
        coEvery { sharedPreferences.getString("username", null) } returns "Ana"
        coEvery { getUserUseCase.invoke() } returns Result.success(
            GetUserResult(userHome = userHomeScreenMock, userProfile = userProfileScreen)
        )

        viewModel.loadUser()
        advanceUntilIdle()

        val user = viewModel.userHome.getOrAwaitValue()
        val userAccounts = user.accounts

        Assert.assertEquals(userHomeScreenMock.accounts, userAccounts)
    }
}