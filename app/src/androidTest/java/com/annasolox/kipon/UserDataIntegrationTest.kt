package com.annasolox.kipon

import android.content.SharedPreferences
import android.util.MainDispatcherRule
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.annasolox.kipon.core.navigation.DetailsAccountScreen
import com.annasolox.kipon.core.navigation.HomeScreen
import com.annasolox.kipon.data.api.models.response.AccountRoleResponse
import com.annasolox.kipon.data.api.models.response.SavingResponse
import com.annasolox.kipon.data.api.models.response.SimplifiedAccountResponse
import com.annasolox.kipon.data.api.models.response.UserProfileResponse
import com.annasolox.kipon.data.api.models.response.UserResponse
import com.annasolox.kipon.data.repository.AccountRepository
import com.annasolox.kipon.data.repository.UserRepository
import com.annasolox.kipon.domain.account.AddUserToAccountUseCase
import com.annasolox.kipon.domain.account.CreateAccountUseCase
import com.annasolox.kipon.domain.account.CreateContributionUseCase
import com.annasolox.kipon.domain.account.GetAccountUseCase
import com.annasolox.kipon.domain.account.UpdateAccountUseCase
import com.annasolox.kipon.domain.image.UploadImageUseCase
import com.annasolox.kipon.domain.user.GetUserUseCase
import com.annasolox.kipon.domain.user.SearchUserUseCase
import com.annasolox.kipon.domain.user.UpdateUserUseCase
import com.annasolox.kipon.ui.screens.AccountDetailScreen
import com.annasolox.kipon.ui.screens.HomeScreen
import com.annasolox.kipon.ui.viewmodels.AccountViewModel
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UserDataIntegrationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockSharedPreferences: SharedPreferences
    private lateinit var mockUserRepository: UserRepository
    private lateinit var mockAccountRepository: AccountRepository
    private lateinit var userViewModel: UserViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var navController: TestNavHostController
    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var uploadImageUseCase: UploadImageUseCase
    private lateinit var updateUserUseCase: UpdateUserUseCase
    private lateinit var searchUserUseCase: SearchUserUseCase
    private lateinit var createAccountUseCase: CreateAccountUseCase
    private lateinit var addUserToAccountUseCase: AddUserToAccountUseCase
    private lateinit var createContributionUseCase: CreateContributionUseCase
    private lateinit var getAccountUseCase: GetAccountUseCase
    private lateinit var updateAccountUseCase: UpdateAccountUseCase

    lateinit var mockUser: UserResponse

    @Before
    fun setup() {
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

        mockUser = UserResponse(
            id = 1L,
            name = "userTest",
            email = "test@example.com",
            registerDate = LocalDate.of(2023, 10, 1),
            profile = UserProfileResponse(
                id = 1L,
                completeName = "Usuario test",
                telephone = "677888999",
                address = "Calle del test, 44",
                photo = ""
            ),
            accountRole = listOf(
                AccountRoleResponse(
                    account = SimplifiedAccountResponse(
                        id = 1L,
                        name = "Hucha 1",
                        currentMoney = 0.0,
                        dateGoal = LocalDate.of(2028, 12, 31),
                        moneyGoal = 5000.0,
                        photo = ""
                    ),
                    role = "Administrador"
                )
            ),
            savings = listOf(
                SavingResponse(
                    id = 1L,
                    userName = "userTest",
                    accountName = "Hucha 1",
                    userId = 1L,
                    amount = 300.0,
                    date = LocalDate.of(2024, 1, 1),
                    userPhoto = "",
                    accountPhoto = "",
                    currentMoney = 300.0
                )
            )
        )

        mockUserRepository = mockk()
        mockAccountRepository = mockk()
        mockSharedPreferences = mockk(relaxed = true)

        every { mockSharedPreferences.getString("username", null) } returns mockUser.name
        coEvery { mockUserRepository.getUserByUsername("userTest") } returns mockUser

        userViewModel = UserViewModel(
            sharedPreferences = mockSharedPreferences,
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

        navController = TestNavHostController(composeTestRule.activity)
        userViewModel.setUserProfile(mockUser)
        userViewModel.setUserHome(mockUser)
        userViewModel.loadUser()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargaNombreUsuarioEnHome()  {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            CompositionLocalProvider { }
            MaterialTheme {
                HomeScreen(
                    navController = navController,
                    accountViewModel = accountViewModel,
                    userViewModel = userViewModel,
                    modifier = Modifier,
                )
            }
        }

        composeTestRule.waitForIdle()

        // Ahora verificamos los datos
        composeTestRule.onNodeWithTag("usernameText").assertIsDisplayed()
        val node = composeTestRule.onNodeWithTag("usernameText").fetchSemanticsNode()
        val texts = node.config.getOrNull(SemanticsProperties.Text)?.joinToString("") ?: ""

        assert(texts.contains("userTest")) {
            "El texto esperado 'userTest' no se encontró en '$texts'"
        }
    }

    @Test
    fun cargarCuentasUsuarioHome()  {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            CompositionLocalProvider { }
            MaterialTheme {
                HomeScreen(
                    navController = navController,
                    accountViewModel = accountViewModel,
                    userViewModel = userViewModel,
                    modifier = Modifier,
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("accountItem").assertIsDisplayed()

        composeTestRule.onNodeWithTag("accountName", useUnmergedTree = true)
            .assertTextContains("Hucha 1")
        composeTestRule.onNodeWithTag("currentMoney", useUnmergedTree = true)
            .assertTextContains("0.0 €")
        composeTestRule.onNodeWithTag("moneyGoal", useUnmergedTree = true)
            .assertTextContains("5000.0 €")
    }

    @Test
    fun navegacionADetalleHucha(){
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            MaterialTheme {
                NavHost(
                    navController = navController,
                    startDestination = HomeScreen
                ) {
                    composable<HomeScreen> {
                        HomeScreen(
                            navController = navController,
                            accountViewModel = accountViewModel,
                            userViewModel = userViewModel,
                            modifier = Modifier,
                        )
                    }
                    composable<DetailsAccountScreen> {
                        AccountDetailScreen(navController, accountViewModel, userViewModel)
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag("accountItem").performClick()
        assertEquals(DetailsAccountScreen::class.qualifiedName, navController.currentDestination?.route)
    }

    @Test
    fun accountDetailLoadingCirculatorCheck(){
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            MaterialTheme {
                AccountDetailScreen(
                    navController = navController,
                    accountViewModel = accountViewModel,
                    userViewModel = userViewModel
                )
            }
        }

        composeTestRule.onNodeWithTag("circularIndicatorAccountDetail").assertIsDisplayed()
    }
}