package com.annasolox.kipon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.annasolox.kipon.core.appModule
import com.annasolox.kipon.data.dataModule
import com.annasolox.kipon.ui.screens.LoginScreen
import com.annasolox.kipon.ui.theme.KiponTheme
import com.annasolox.kipon.ui.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Start koin
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule, dataModule, viewModelModule)
        }
        setContent {
            KiponTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center){
                        LoginScreen()
                    }
                }
            }
        }
    }
}