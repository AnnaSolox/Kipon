package com.annasolox.kipon.ui.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.annasolox.kipon.R
import com.annasolox.kipon.ui.composables.buttons.OptionsButton
import com.annasolox.kipon.ui.composables.textFields.FormTextField
import com.annasolox.kipon.ui.composables.textFields.LoginPasswordTextField
import com.annasolox.kipon.ui.composables.textFields.PhotoTextField
import com.annasolox.kipon.ui.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by userViewModel.userProfile.observeAsState()

    //Nested scroll
    val maxSize = 300.dp
    val minSize = 100.dp
    var currentBoxSize by remember { mutableStateOf(maxSize) }
    var infoImageElementsAlpha by remember { mutableFloatStateOf(1f) }

    //Profile info
    val username by userViewModel.username.observeAsState()
    val email by userViewModel.email.observeAsState()
    val emailError by userViewModel.emailError.observeAsState()
    val phone by userViewModel.phone.observeAsState()
    val phoneError by userViewModel.phoneError.observeAsState()
    val address by userViewModel.address.observeAsState()
    val addressError by userViewModel.addressError.observeAsState()
    val password by userViewModel.password.observeAsState()
    val passwordError by userViewModel.passwordError.observeAsState()
    val photo by userViewModel.photo.observeAsState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                //Calcular el cambio de tamaño de la caja basado en el delta scroll
                val delta = available.y
                val newBoxSize = currentBoxSize + delta.dp
                val previusBoxSize = currentBoxSize

                //Constraints para el tamaño de la imagen según los límites
                currentBoxSize = newBoxSize.coerceIn(minSize, maxSize)
                val consumed = currentBoxSize - previusBoxSize

                //Cálculo del alpha para hacer desaparecer la info de la imagen expandida
                val range = maxSize - minSize
                val progress = (currentBoxSize - minSize) / range

                infoImageElementsAlpha = progress.coerceIn(0f, 1f)

                return Offset(0f, consumed.value)
            }
        }
    }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val imageBytes = inputStream?.readBytes()
            inputStream?.close()

            imageBytes?.let {
                userViewModel.uploadImage(it)
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        }
    }


    val requiresPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    var editEnable by remember { mutableStateOf(false) }
    var isProfileScreen = true

    Box(Modifier.nestedScroll(nestedScrollConnection)) {

        AnimatedVisibility(
            visible = currentUser != null,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 500)
            )
        ) {

            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        state = rememberScrollState()
                    )
                    .padding(15.dp)
                    .padding(bottom = 16.dp)
                    .offset {
                        IntOffset(0, currentBoxSize.roundToPx())
                    },
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "User information".uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                )

                FormTextField(
                    enabled = false,
                    value = username ?: "",
                    label = "Username",
                    error = null,
                    modifier = Modifier.fillMaxWidth()
                ) { userViewModel.onUsernameChanged(it) }

                FormTextField(
                    enabled = editEnable,
                    value = email ?: "",
                    label = "Email",
                    error = emailError,
                    modifier = Modifier.fillMaxWidth()
                ) { userViewModel.onEmailChanged(it) }

                LoginPasswordTextField(
                    enabled = false,
                    isProfileScreen = isProfileScreen,
                    password = password ?: "fakePassword",
                    error = null,
                    modifier = Modifier.fillMaxWidth()
                ) { userViewModel.onPasswordChanged(it) }

                Text(
                    "Personal information".uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                )

                FormTextField(
                    enabled = false,
                    value = currentUser!!.profile.completeName,
                    label = "Complete name",
                    error = null,
                    modifier = Modifier.fillMaxWidth()
                ) { }

                FormTextField(
                    enabled = editEnable,
                    value = phone ?: "",
                    label = "Phone number",
                    error = phoneError,
                    modifier = Modifier.fillMaxWidth()
                ) { userViewModel.onPhoneChanged(it) }

                FormTextField(
                    enabled = editEnable,
                    value = address ?: "",
                    label = "Adress",
                    error = addressError,
                    modifier = Modifier.fillMaxWidth()
                ) { userViewModel.onAddressChanged(it) }

                PhotoTextField(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = editEnable,
                    value = photo ?: "",
                    onValueChange = { userViewModel.onPhotoChanged(it) },
                    label = "Photo URL",
                    error = null,
                ) {
                    if (requiresPermission) {
                        permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                    } else {
                        imagePickerLauncher.launch("image/*")
                    }
                }
                if (editEnable) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 120.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Button({
                            editEnable = false
                            userViewModel.populateProfileFields()
                        }) {
                            Text("Discard")
                        }
                        Spacer(Modifier.size(8.dp))
                        Button({
                            userViewModel.updateUserInfo()
                            editEnable = false
                        }) {
                            Text("Save")
                        }
                    }
                }
            }
        }




        Column(
            Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
        ) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(currentBoxSize)
                    .graphicsLayer {
                        clip = true
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    },
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(currentBoxSize)
                ) {
                    AsyncImage(
                        model = currentUser?.profile?.photo,
                        error = painterResource(R.drawable.girl_photo),
                        contentDescription = "User profile image header",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.45f))
                    )
                }

                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .graphicsLayer(
                            alpha = infoImageElementsAlpha
                        ),
                    verticalAlignment = Alignment.Bottom
                ) {

                    Column(
                        Modifier
                            .weight(1f)
                    ) {
                        Text(
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            text = "Fecha de registro"
                        )
                        Spacer(Modifier.size(4.dp))
                        Text(
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            text = currentUser?.registrationDate.toString()
                        )
                    }
                    OptionsButton(Icons.Filled.Edit) {
                        editEnable = !editEnable
                    }
                }

                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .graphicsLayer(alpha = 1f - infoImageElementsAlpha),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        currentUser!!.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                    )
                }
            }
        }
    }
}