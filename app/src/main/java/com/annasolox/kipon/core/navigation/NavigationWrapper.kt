package com.annasolox.kipon.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.annasolox.kipon.ui.composables.BottomNavigationBar
import com.annasolox.kipon.ui.screens.AccountDetailScreen
import com.annasolox.kipon.ui.screens.HomeScreen
import com.annasolox.kipon.ui.screens.LoginScreen
import com.annasolox.kipon.ui.screens.ProfileScreen
import com.annasolox.kipon.ui.screens.RegisterScreen
import com.annasolox.kipon.ui.screens.SearchUsersScreen
import com.annasolox.kipon.ui.screens.TransactionsScreen
import com.annasolox.kipon.ui.viewmodels.AccountViewModel
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val accountViewModel: AccountViewModel = koinViewModel()
    val userViewModel: UserViewModel = koinViewModel()

    val bottomBarScreens = listOf(
        HomeScreen::class.qualifiedName,
        TransactionsScreen::class.qualifiedName,
        ProfileScreen::class.qualifiedName
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomBarScreens

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { if (showBottomBar) {
            BottomNavigationBar(navController)
        }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginScreen,
            modifier = Modifier.background(Color.White).padding(innerPadding)
        ) {
            composable<LoginScreen> {
                LoginScreen(navController)
            }

            composable<RegisterScreen> {
                RegisterScreen(navController)
            }

            composable<HomeScreen> {
                HomeScreen(navController, accountViewModel, userViewModel)
            }

            composable<DetailsAccountScreen> {
                AccountDetailScreen(navController, accountViewModel)
            }

            composable<TransactionsScreen> {
                TransactionsScreen(userViewModel)
            }

            composable<ProfileScreen> {
                ProfileScreen(navController, userViewModel)
            }

            composable<SearchUsersScreen>{
                SearchUsersScreen(navController, userViewModel)
            }
        }
    }
}