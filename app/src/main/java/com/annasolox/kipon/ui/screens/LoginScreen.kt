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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.annasolox.kipon.R
import com.annasolox.kipon.core.navigation.RegisterScreen

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordHidden by remember { mutableStateOf(true) }

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
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    singleLine = true,
                    label = { Text(text = "Username") },
                    shape = RoundedCornerShape(100f)
                )

                Spacer(Modifier.size(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Password") },
                    singleLine = true,
                    shape = RoundedCornerShape(100f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton({ passwordHidden = !passwordHidden }) {
                            Icon(
                                painter = painterResource(if (passwordHidden) R.drawable.visibility_on else R.drawable.visibility_off),
                                contentDescription = "Visibility icon"
                            )
                        }
                    }
                )

                Spacer(Modifier.size(12.dp))

                Text(text = "¿Has olvidado la contraseña?")

                Spacer(Modifier.size(60.dp))

                Button(
                    {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(text = "Iniciar sesión")
                }

                Spacer(Modifier.size(12.dp))

                Row {
                    Text(text = "¿No tienes cuenta?")
                    Spacer(Modifier.size(6.dp))
                    Text(
                        text = "Regístrate",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.clickable {
                            navController.navigate(
                                RegisterScreen
                            )
                        }
                    )
                }
            }
        }
    }
}