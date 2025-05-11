package com.annasolox.kipon.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.annasolox.kipon.ui.screens.LoginScreen
import com.annasolox.kipon.ui.screens.RegisterScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginScreen){
        composable<LoginScreen> {
            LoginScreen(navController)
        }

        composable<RegisterScreen> {
            RegisterScreen(navController)
        }
    }
}