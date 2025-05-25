package com.annasolox.kipon

import android.content.SharedPreferences
import android.util.LiveDataTestUtils.getOrAwaitNonEmptyValue
import android.util.LiveDataTestUtils.getOrAwaitValue
import android.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.rules.TestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.annasolox.kipon.data.api.models.response.AccountResponse
import com.annasolox.kipon.data.api.models.response.AccountRoleResponse
import com.annasolox.kipon.data.api.models.response.SavingResponse
import com.annasolox.kipon.data.api.models.response.SimplifiedAccountResponse
import com.annasolox.kipon.data.api.models.response.UserProfileResponse
import com.annasolox.kipon.data.api.models.response.UserResponse
import com.annasolox.kipon.data.api.models.response.UserRolResponse
import com.annasolox.kipon.data.api.models.response.UserSimplified
import com.annasolox.kipon.data.repository.AccountRepository
import com.annasolox.kipon.data.repository.ImageUploadRepository
import com.annasolox.kipon.data.repository.UserRepository
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
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class UserDataUnitTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val accountRepository = mockk<AccountRepository>(relaxed = true)
    private val imageUploadRepository = mockk<ImageUploadRepository>(relaxed = true)
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

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatcherRule.testDispatcher)
        viewModel = UserViewModel(userRepository, imageUploadRepository, sharedPreferences)
        accountViewModel = AccountViewModel(accountRepository, userRepository, imageUploadRepository)

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
                    role ="Aministrador"
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
                    date = LocalDate.of(2030,10,26),
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
                    date = LocalDate.of(2030,10,27),
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

        val result = viewModel.attemptUpdateUserInfo()

        Assert.assertTrue(result)
        Assert.assertNull(viewModel.emailError.value)
        Assert.assertNull(viewModel.phoneError.value)
        Assert.assertNull(viewModel.addressError.value)
    }

    @Test
    fun informacionActualizarUsuarioFallo() = runTest {
        viewModel.onEmailChanged("usuarioejemplo.com") //Email inválido
        viewModel.onPhoneChanged("123456789")
        viewModel.onAddressChanged("Calle ejemplo 123")

        val result = viewModel.attemptUpdateUserInfo()
        Assert.assertFalse(result)
    }

    @Test
    fun busquedaUsuarioNombreParcialExito() = runTest {
        val nombreParcial = "Ana"
        val usuariosMock = listOf(
            SearchedUser(id = 1, userName = "Ana Pérez", photo = null),
            SearchedUser(id = 2, userName = "Anastasia Gómez", photo = null)
        )

        coEvery { userRepository.fetchUsersByPartialName(nombreParcial) } returns usuariosMock
        viewModel.searchUsers(nombreParcial)
        advanceUntilIdle()

        val result = viewModel.fetchedUsers.getOrAwaitNonEmptyValue()

        Assert.assertEquals(usuariosMock, result)
    }

    @Test
    fun cargaAhorrosUsuarioExito() = runTest {

        coEvery { sharedPreferences.getString("username", null) } returns "Ana"
        coEvery { userRepository.getUserByUsername("Ana") } returns userResponseMock

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
        coEvery { userRepository.getUserByUsername("Ana") } returns userResponseMock

        viewModel.loadUser()
        advanceUntilIdle()

        val userProfile = viewModel.userProfile.getOrAwaitValue()

        Assert.assertEquals(userProfileScreen.profile.photo, userProfile.profile.photo)
    }

    @Test
    fun cargarCuentasUsuarioExito() = runTest {
        coEvery { sharedPreferences.getString("username", null) } returns "Ana"
        coEvery { userRepository.getUserByUsername("Ana") } returns userResponseMock

        viewModel.loadUser()
        advanceUntilIdle()

        val user = viewModel.userHome.getOrAwaitValue()
        val userAccounts = user.accounts

        Assert.assertEquals(userHomeScreenMock.accounts, userAccounts)
    }

    @Test
    fun cargarCuentaActualExito() = runTest {
        val accountId = 1L

        coEvery { accountRepository.getAccountById(accountId) } returns accountResponseMock

        accountViewModel.loadCurrentAccount(accountId)
        advanceUntilIdle()

        val result = accountViewModel.currentAccount.getOrAwaitValue()

        Assert.assertEquals(mockAccount, result)
    }

}