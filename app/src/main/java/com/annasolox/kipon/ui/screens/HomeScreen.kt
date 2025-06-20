package com.annasolox.kipon.ui.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.annasolox.kipon.R
import com.annasolox.kipon.core.navigation.AccountNavigationEvent.NavigateToAccountDetail
import com.annasolox.kipon.core.navigation.DetailsAccountScreen
import com.annasolox.kipon.core.navigation.HomeScreen
import com.annasolox.kipon.ui.composables.AccountSearchBar
import com.annasolox.kipon.ui.composables.accounts.AccountElevatedCard
import com.annasolox.kipon.ui.composables.images.ImageThumbnail
import com.annasolox.kipon.ui.composables.textFields.DatePickerTextField
import com.annasolox.kipon.ui.composables.textFields.FormTextField
import com.annasolox.kipon.ui.composables.textFields.PhotoTextField
import com.annasolox.kipon.ui.viewmodels.AccountViewModel
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    accountViewModel: AccountViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }
    val user by userViewModel.userHome.observeAsState()

    //Modal bottom sheet
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }

    //navigation event
    val navEvent by accountViewModel.navigationEvent.observeAsState()

    //Creation account form fields
    val accountName by accountViewModel.name.observeAsState("")
    val moneyGoal by accountViewModel.moneyGoal.observeAsState(null)
    val dateGoal by accountViewModel.dateGoal.observeAsState(null)
    val photo by accountViewModel.photo.observeAsState()

    //Creation account form errors
    val accountNameError by accountViewModel.nameError.observeAsState()
    val moneyGoalError by accountViewModel.moneyGoalError.observeAsState()
    val dateGoalError by accountViewModel.dateGoalError.observeAsState()
    val creationAccountValidation by accountViewModel.isValidAccountCreate.observeAsState()

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val imageBytes = inputStream?.readBytes()
            inputStream?.close()

            imageBytes?.let {
                accountViewModel.uploadImage(it)
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

    LaunchedEffect(Unit) {
        userViewModel.loadUser()
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

    LaunchedEffect(navEvent) {
        accountViewModel.navigationEvent.value?.let {
            event ->
            when(event){
                NavigateToAccountDetail -> {
                    navController.navigate(DetailsAccountScreen){
                        launchSingleTop = true
                    }
                    accountViewModel.clearCreateForm()
                    accountViewModel.clearNavigationEvent()
                }
            }
        }
    }

    AnimatedVisibility(
        visible = user != null,
        enter = fadeIn(tween(300))
    ) {
        
        Column(
            modifier
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
                            stringResource(R.string.greeting_home_text) + " ${user?.userName}!",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.testTag("usernameText")
                        )
                        Text(stringResource(R.string.welcome_back_text), style = MaterialTheme.typography.bodySmall)
                    }
                }
                Row {
                    IconButton(
                        onClick = {
                            isSheetOpen = true
                            coroutineScope.launch { sheetState.show() }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(1.dp, Color.Black, shape = CircleShape)
                    ) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = stringResource(R.string.cd_add_icon))
                    }
                }
            }

            AccountSearchBar(
                query = query,
                onQueryChange = { query = it }
            )

            if(user?.accounts?.isEmpty() == true){
                Box(Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.TopStart){
                    Text(stringResource(R.string.no_accounts_yet))
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    val filteredAccounts = user?.accounts?.filter {
                        it.name.contains(query, ignoreCase = true)
                    } ?: emptyList()

                    items(filteredAccounts) { account ->
                        Spacer(Modifier.size(4.dp))
                        AccountElevatedCard(
                            account,
                            account.photo ?: "",
                            Modifier.testTag("accountItem").clickable(onClick = {
                                accountViewModel.loadCurrentAccount(account.id)
                                navController.navigate(DetailsAccountScreen) {
                                    popUpTo(HomeScreen) { inclusive = true }
                                    launchSingleTop = true
                                }
                            })
                        )
                        Spacer(Modifier.size(4.dp))
                    }
                }
            }


            if (isSheetOpen) {

                ModalBottomSheet(
                    onDismissRequest = {
                        isSheetOpen = false
                        accountViewModel.clearCreateForm()
                        accountViewModel.clearErrors()
                                       },
                    sheetState = sheetState,
                    containerColor = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            stringResource(R.string.create_new_account_title).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                        )

                        FormTextField(
                            modifier = Modifier.width(300.dp),
                            value = accountName,
                            label = stringResource(R.string.account_name_label),
                            error = accountNameError,
                        ) { accountViewModel.onAccountNameChange(it) }

                        FormTextField(
                            modifier = Modifier.width(300.dp),
                            value = moneyGoal?.toString() ?: "",
                            label = stringResource(R.string.money_goal_label),
                            error = moneyGoalError,
                            keyboardType = KeyboardType.Number
                        ) {
                            accountViewModel.onAccountMoneyGoalChange(it.toDouble()) }

                        DatePickerTextField(
                            modifier = Modifier.width(300.dp),
                            dateGoal,
                            onDateSelected = {accountViewModel.onAccountDateGoalChange(it)},
                            label = stringResource(R.string.date_goal_label),
                            error = dateGoalError
                        )

                        PhotoTextField(
                            modifier = Modifier.width(300.dp),
                            enabled = true,
                            value = photo ?: "",
                            onValueChange = { accountViewModel.onAccountPhotoChange(it) },
                            label = stringResource(R.string.photo_url_label),
                            error = null,
                        ) {
                            if (requiresPermission) {
                                permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                            } else {
                                imagePickerLauncher.launch("image/*")
                            }
                        }

                        Spacer(Modifier.size(8.dp))

                        Button(
                            onClick = {
                                accountViewModel.createNewAccount()
                                if(creationAccountValidation == true){
                                    isSheetOpen = false
                                    accountViewModel.clearCreateForm()
                                    accountViewModel.clearErrors()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Text(stringResource(R.string.create_account_btn_text))
                        }
                    }
                }
            }
        }
    }
}
