package com.annasolox.kipon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.ui.composables.textFields.RegisterPasswordTextField
import com.annasolox.kipon.ui.composables.textFields.RegisterTextField
import com.annasolox.kipon.ui.viewmodels.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(authViewModel: AuthViewModel = koinViewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var completeName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var adress by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }

    val diagonalGradient = Brush.linearGradient(
        colors = listOf(
            Color.White,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        ),
        start = Offset(150f, 150f),
        end = Offset(2000f, 2000f)
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(diagonalGradient)
    ) {

        Box(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .width(380.dp)
                    .padding(start = 16.dp, end = 16.dp, top = 42.dp, bottom = 75.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            )
            {
                RegisterTextField(username, "Username") { username = it }

                Spacer(Modifier.size(6.dp))

                RegisterPasswordTextField(password, "Password") { password = it }

                Spacer(Modifier.size(6.dp))

                RegisterPasswordTextField(
                    passwordConfirmation,
                    "Password confirmation"
                ) { passwordConfirmation = it }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(email, "Email") { email = it }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(completeName, "Complete name") { completeName = it }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(phoneNumber, "Phone number") { phoneNumber = it }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(adress, "Address") { adress = it }

                Spacer(Modifier.size(40.dp))

                Button(
                    {
                        authViewModel.register(
                            userName = username,
                            password = password,
                            email = email,
                            completeName = completeName,
                            phone = phoneNumber,
                            address = adress,
                            photo = null,
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Register")
                }

                Spacer(Modifier.size(12.dp))

                Row {
                    Text(text = "¿Ya estás registrado?")
                    Spacer(Modifier.size(6.dp))
                    Text(
                        text = "Inicia sesión",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.clickable {})
                }
            }
        }
    }
}


