package com.annasolox.kipon.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.pm.ShortcutInfoCompat
import com.annasolox.kipon.ui.composables.BottomNavigationBar

@Composable
fun BottomNavScreen() {
    Surface(
        Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar() }
        ) { innerPadding ->
            HomeScreen()
        }

    }
}