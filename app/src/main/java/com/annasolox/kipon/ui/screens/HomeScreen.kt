package com.annasolox.kipon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(userViewModel: UserViewModel = koinViewModel()) {
    val user by userViewModel.userHome.observeAsState(null)

    Box(Modifier.fillMaxSize().background(Color.Gray)){
        Text(user?.userName ?: "Nombre vacío")
    }
}