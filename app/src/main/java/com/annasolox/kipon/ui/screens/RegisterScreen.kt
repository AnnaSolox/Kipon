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
import androidx.compose.runtime.livedata.observeAsState
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
    val username by authViewModel.userName.observeAsState("")
    val usernameError by authViewModel.userNameError.observeAsState()
    val password by authViewModel.password.observeAsState("")
    val passwordError by authViewModel.passwordError.observeAsState()
    val email by authViewModel.email.observeAsState("")
    val emailError by authViewModel.emailError.observeAsState()
    val completeName by authViewModel.completeName.observeAsState("")
    val completeNameError by authViewModel.completeNameError.observeAsState()
    val phoneNumber by authViewModel.phone.observeAsState("")
    val phoneNumberError by authViewModel.phoneError.observeAsState()
    val adress by authViewModel.address.observeAsState("")
    val addressError by authViewModel.addressError.observeAsState()
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
                RegisterTextField(username, "Username", usernameError) { authViewModel.onUserNameChanged(it) }

                Spacer(Modifier.size(6.dp))

                RegisterPasswordTextField(password, "Password", passwordError) { authViewModel.onPasswordChanged(it) }

                Spacer(Modifier.size(6.dp))

                RegisterPasswordTextField(passwordConfirmation,"Password confirmation", passwordError) { passwordConfirmation = it }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(email, "Email", emailError) { authViewModel.onEmailChanged(it) }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(completeName, "Complete name", completeNameError) { authViewModel.onCompleteNameChanged(it) }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(phoneNumber, "Phone number", phoneNumberError) { authViewModel.onPhoneChanged(it) }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(adress, "Address", addressError) { authViewModel.onAddressChanged(it) }

                Spacer(Modifier.size(40.dp))

                Button(
                    {
                        authViewModel.register()
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


