package com.annasolox.kipon.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.annasolox.kipon.core.navigation.DetailsAccountScreen
import com.annasolox.kipon.core.navigation.HomeScreen
import com.annasolox.kipon.ui.composables.AccountSearchBar
import com.annasolox.kipon.ui.composables.accounts.AccountElevatedCard
import com.annasolox.kipon.ui.composables.images.ImageThumbnail
import com.annasolox.kipon.ui.viewmodels.AccountViewModel
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    accountViewModel: AccountViewModel,
    userViewModel: UserViewModel = koinViewModel()
) {

    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }
    val user by userViewModel.userHome.observeAsState()

    //Modal bottom sheet
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        accountViewModel.clearCurrentAccount()
    }

    if (user == null) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    AnimatedVisibility(
        visible = user != null,
        enter = fadeIn(tween(300))
    ) {
        
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                }
                .padding(8.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ImageThumbnail(Modifier.size(60.dp), user?.photoUrl)
                    Spacer(Modifier.size(8.dp))
                    Column {
                        Text(
                            "¡Hola ${user?.userName}!",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black
                        )
                        Text("Bienvenida de nuevo", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Row {
                    IconButton(
                        onClick = {/*TODO*/ },
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(1.dp, Color.Black, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notification icon"
                        )
                    }
                    Spacer(Modifier.size(6.dp))
                    IconButton(
                        onClick = {
                            isSheetOpen = true
                            coroutineScope.launch { sheetState.show() }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(1.dp, Color.Black, shape = CircleShape)
                    ) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add icon")
                    }
                }
            }

            AccountSearchBar(
                query,
                onQueryChange = { query = it }
            )

            LazyColumn {
                val filteredAccounts = user!!.accounts.filter {
                    it.name.contains(query, ignoreCase = true)
                }
                items(filteredAccounts) { account ->
                    AccountElevatedCard(
                        account,
                        account.photo ?: "",
                        Modifier.clickable(onClick = {
                            accountViewModel.loadCurrentAccount(account.id)
                            navController.navigate(DetailsAccountScreen) {
                                popUpTo(HomeScreen) { inclusive = true }
                                launchSingleTop = true
                            }
                        })
                    )
                    Spacer(Modifier.size(8.dp))
                }
            }

            if (isSheetOpen) {
                ModalBottomSheet(
                    onDismissRequest = { isSheetOpen = false },
                    sheetState = sheetState,
                    containerColor = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Create new account", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.size(8.dp))

                        /*FormTextField(
                            username,
                            usernameError
                        ) { authViewModel.onUserNameChanged(it) }*/

                        Button(
                            onClick = {
                                isSheetOpen = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}
