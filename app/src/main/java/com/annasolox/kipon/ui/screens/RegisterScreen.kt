package com.annasolox.kipon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.annasolox.kipon.R
import com.annasolox.kipon.core.navigation.LoginScreen
import com.annasolox.kipon.core.navigation.RegisterScreen
import com.annasolox.kipon.ui.composables.backgrounds.AuthBackground
import com.annasolox.kipon.ui.composables.textFields.RegisterPasswordTextField
import com.annasolox.kipon.ui.composables.textFields.RegisterTextField
import com.annasolox.kipon.ui.models.LoginUiState
import com.annasolox.kipon.ui.viewmodels.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = koinViewModel()
) {
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
    val address by authViewModel.address.observeAsState("")
    val addressError by authViewModel.addressError.observeAsState()
    val passwordConfirmation by authViewModel.passwordConfirmation.observeAsState("")
    val passwordConfirmationError by authViewModel.passwordConfirmationError.observeAsState("")
    val isLogin by authViewModel.loginState.observeAsState()

    LaunchedEffect(isLogin) {
        if (isLogin is LoginUiState.Success)
            navController.navigate(LoginScreen) {
                popUpTo<RegisterScreen> { inclusive = true }
                launchSingleTop = true
            }
    }

    AuthBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .background(Color.White)
                    .width(380.dp)
                    .padding(start = 16.dp, end = 16.dp, top = 42.dp, bottom = 75.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            )
            {
                RegisterTextField(
                    username,
                    stringResource(R.string.username_label),
                    usernameError
                ) { authViewModel.onUserNameChanged(it) }

                Spacer(Modifier.size(6.dp))

                RegisterPasswordTextField(
                    password,
                    stringResource(R.string.password_label),
                    passwordError
                ) { authViewModel.onPasswordChanged(it) }

                Spacer(Modifier.size(6.dp))

                RegisterPasswordTextField(
                    passwordConfirmation,
                    stringResource(R.string.password_confirmation_label), passwordConfirmationError
                ) { authViewModel.onPasswordConfirmationChanged(it) }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(
                    email,
                    stringResource(R.string.email_label),
                    emailError
                ) { authViewModel.onEmailChanged(it) }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(
                    completeName,
                    stringResource(R.string.complete_name_label),
                    completeNameError
                ) { authViewModel.onCompleteNameChanged(it) }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(
                    phoneNumber,
                    stringResource(R.string.phone_number_label),
                    phoneNumberError
                ) { authViewModel.onPhoneChanged(it) }

                Spacer(Modifier.size(6.dp))

                RegisterTextField(
                    address,
                    stringResource(R.string.address_label),
                    addressError
                ) { authViewModel.onAddressChanged(it) }

                Spacer(Modifier.size(40.dp))

                Box(
                    Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (isLogin) {
                        is LoginUiState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is LoginUiState.Error -> {
                            Text(
                                text = stringResource(R.string.register_error_message),
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        else -> {}
                    }
                }

                Button(
                    {
                        authViewModel.register()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(stringResource(R.string.register_btn_text))
                }

                Spacer(Modifier.size(12.dp))

                Row {
                    Text(text = stringResource(R.string.are_you_already_registered_text))
                    Spacer(Modifier.size(6.dp))
                    Text(
                        text = stringResource(R.string.login_button),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.clickable {
                            navController.navigate(LoginScreen) {
                                popUpTo<RegisterScreen> { inclusive = true }
                                launchSingleTop = true
                            }
                        })
                }
            }
        }
    }
}


