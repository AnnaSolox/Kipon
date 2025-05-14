package com.annasolox.kipon.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.annasolox.kipon.ui.screens.AccountDetailScreen
import com.annasolox.kipon.ui.screens.BottomNavScreen
import com.annasolox.kipon.ui.screens.HomeScreen
import com.annasolox.kipon.ui.screens.LoginScreen
import com.annasolox.kipon.ui.screens.RegisterScreen
import com.annasolox.kipon.ui.viewmodels.AccountViewModel
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val accountViewModel: AccountViewModel = koinViewModel()
    val userViewModel: UserViewModel = koinViewModel()


    NavHost(navController = navController, startDestination = LoginScreen){
        composable<LoginScreen> {
            LoginScreen(navController)
        }

        composable<RegisterScreen> {
            RegisterScreen(navController)
        }

        composable<HomeScreen> {
            HomeScreen(navController, accountViewModel, userViewModel)
        }

        composable<BottomNavscreen>{
            BottomNavScreen(navController, accountViewModel, userViewModel)
        }

        composable<DetailsAccountScreen> {
            AccountDetailScreen(navController, accountViewModel)
        }
    }
}