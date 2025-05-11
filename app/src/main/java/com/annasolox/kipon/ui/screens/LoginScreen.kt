package com.annasolox.kipon.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.annasolox.kipon.R
import com.annasolox.kipon.core.navigation.LoginScreen
import com.annasolox.kipon.core.navigation.RegisterScreen
import com.annasolox.kipon.ui.composables.backgrounds.AuthBackground
import com.annasolox.kipon.ui.composables.textFields.LoginPasswordTextField
import com.annasolox.kipon.ui.composables.textFields.LoginUsernameTextField
import com.annasolox.kipon.ui.viewmodels.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = koinViewModel()) {
    val username by authViewModel.userName.observeAsState("")
    val usernameError by authViewModel.userNameError.observeAsState()
    val password by authViewModel.password.observeAsState("")
    val passwordError by authViewModel.passwordError.observeAsState()
    val state = authViewModel.loginState.observeAsState()

    AuthBackground {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (logo, form) = createRefs()
            Image(
                painter = painterResource(R.drawable.logo_claim_kipon_h),
                contentDescription = "Logo Kipon",
                modifier = Modifier
                    .clipToBounds()
                    .size(200.dp)
                    .constrainAs(logo) {
                        top.linkTo(parent.top)
                        bottom.linkTo(form.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

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
                    .constrainAs(form) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .width(380.dp)
                        .padding(start = 16.dp, end = 16.dp, top = 42.dp, bottom = 75.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                )
                {
                    LoginUsernameTextField(
                        username,
                        usernameError
                    ) { authViewModel.onUserNameChanged(it) }

                    Spacer(Modifier.size(12.dp))

                    LoginPasswordTextField(
                        password,
                        passwordError
                    ) { authViewModel.onPasswordChanged(it) }

                    //Spacer(Modifier.size(12.dp))
                    //Text(text = "¿Has olvidado la contraseña?")

                    Spacer(Modifier.size(60.dp))

                    Button(
                        { authViewModel.login() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(text = "Iniciar sesión")
                    }

                    Spacer(Modifier.size(12.dp))

                    Row {
                        Text(text = "Don't have an account?")
                        Spacer(Modifier.size(6.dp))
                        Text(
                            text = "Register",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.clickable {
                                navController.navigate(RegisterScreen) {
                                    popUpTo<LoginScreen>{inclusive = true}
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}