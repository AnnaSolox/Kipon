package com.annasolox.kipon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.annasolox.kipon.ui.screens.AccountDetailScreen
import com.annasolox.kipon.ui.screens.LoginScreen
import com.annasolox.kipon.ui.screens.RegisterScreen
import com.annasolox.kipon.ui.theme.KiponTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KiponTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center){
                        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
                            RegisterScreen()
                        }
                    }
                }
            }
        }
    }
}