package com.annasolox.kipon.ui.screens

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.annasolox.kipon.R
import com.annasolox.kipon.core.navigation.HomeScreen
import com.annasolox.kipon.core.navigation.SearchUsersScreen
import com.annasolox.kipon.ui.composables.accounts.LazyAccountContributions
import com.annasolox.kipon.ui.composables.buttons.OptionsButton
import com.annasolox.kipon.ui.composables.headers.ColumnAccountDetailInfo
import com.annasolox.kipon.ui.composables.textFields.DatePickerTextField
import com.annasolox.kipon.ui.composables.textFields.FormTextField
import com.annasolox.kipon.ui.composables.textFields.PhotoTextField
import com.annasolox.kipon.ui.viewmodels.AccountViewModel
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(
    navController: NavController,
    accountViewModel: AccountViewModel,
    userViewModel: UserViewModel
) {
    //currentAccount
    val currentAccount by accountViewModel.currentAccount.observeAsState()
    Log.d("AccountDetailScreen", "currentAccount: $currentAccount")

    //Contribution form field
    val contribAmount by accountViewModel.contributionAmount.observeAsState()

    //Current user
    val currentUser by userViewModel.userHome.observeAsState()

    //Nested scroll
    val maxSize = 300.dp
    val minSize = 100.dp
    var currentBoxSize by remember { mutableStateOf(maxSize) }
    var infoImageElementsAlpha by remember { mutableFloatStateOf(1f) }

    val savings by accountViewModel.savingsList.observeAsState()
    val currentAccountAmount by accountViewModel.currentAccountAmount.observeAsState()
    val contributionAmountError by accountViewModel.contributionAmountError.observeAsState()
    val contributionValidation by accountViewModel.isValidContributionCreate.observeAsState()
    val editAccountValidation by accountViewModel.isValidEditAccount.observeAsState()
    val editPhoto by accountViewModel.editAccountPhoto.observeAsState()

    val coroutineScope = rememberCoroutineScope()
    val addSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isAddSheetOpen by remember { mutableStateOf(false) }
    val editSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isEditSheetOpen by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(savings) {
        if (!savings.isNullOrEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(editAccountValidation) {
        if (editAccountValidation == true) {
            coroutineScope.launch {
                editSheetState.hide()
                isEditSheetOpen = false
                accountViewModel.resetEditAccountValidation()
            }
        }
    }

    LaunchedEffect(contributionValidation) {
        if (contributionValidation == true){
            coroutineScope.launch {
                addSheetState.hide()
                isAddSheetOpen = false
            }
        }
    }

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

    BackHandler {
        navController.navigate(HomeScreen) {
            popUpTo(HomeScreen) {
                inclusive = true
            }
        }
    }

    LaunchedEffect(currentAccount) {
        if (currentAccount != null) {
            accountViewModel.loadingSavingsList()
        }
    }

    Box(Modifier.fillMaxSize()) {

        if (currentAccount == null) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        Box(Modifier.nestedScroll(nestedScrollConnection)) {

            AnimatedVisibility(
                visible = currentAccount != null,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 500)
                )
            ) {
                savings?.let {
                    LazyAccountContributions(
                        currentBoxSize,
                        savings!!,
                        currentAccount!!,
                        currentAccountAmount ?: 0.0,
                        listState = listState
                    )
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
                                model = "${currentAccount!!.photo}",
                                error = painterResource(R.drawable.placeholder_image),
                                contentDescription = stringResource(R.string.cd_user_image_thumbnail),
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
                                ColumnAccountDetailInfo(
                                    members = currentAccount!!.userMembers,
                                    title = currentAccount!!.name,
                                    currentAccount!!.dateGoal
                                )
                            }
                            if(currentUser!!.userName == currentAccount!!.admin){
                                Row{
                                    OptionsButton(Icons.Filled.People){
                                        navController.navigate(SearchUsersScreen)
                                    }
                                    Spacer(Modifier.size(8.dp))
                                    OptionsButton(Icons.Filled.Edit){
                                        isEditSheetOpen = true
                                        coroutineScope.launch { editSheetState.show() }
                                    }
                                }
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
                                currentAccount!!.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                            )
                        }
                    }
                }
            }
        }

        IconButton({ navController.navigate(HomeScreen) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back_button),
                modifier = Modifier.size(30.dp),
                tint = Color.White
            )
        }

        FloatingActionButton(
            onClick = {
                isAddSheetOpen = true
                coroutineScope.launch { addSheetState.show() }
            },
            Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        )
        {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.cd_add_new_contribution_icon)
            )
        }

        val editAccountName by accountViewModel.editAccountName.observeAsState()
        val editAccountNameError by accountViewModel.editAccountNameError.observeAsState()
        val editMoneyGoal by accountViewModel.editAccountMoneyGoal.observeAsState()
        val editMoneyGoalError by accountViewModel.editAccountMoneyGoalError.observeAsState()
        val editDateGoal by accountViewModel.editAccountDateGoal.observeAsState()
        val editDateGoalError by accountViewModel.editAccountDateGoalError.observeAsState()

        if (isEditSheetOpen) {

            ModalBottomSheet(
                onDismissRequest = {
                    isEditSheetOpen = false
                    accountViewModel.clearEditAccountError()
                    accountViewModel.populateEditAccountForm()
                },
                sheetState = editSheetState,
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
                        "Edit account".uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                    )

                    FormTextField(
                        modifier = Modifier.width(300.dp),
                        value = editAccountName ?: "",
                        label = stringResource(R.string.account_name_label),
                        placeholder = currentAccount!!.name,
                        error = editAccountNameError,
                    ) {
                        accountViewModel.onEditAccountNameChange(it)
                    }

                    FormTextField(
                        modifier = Modifier.width(300.dp),
                        value = editMoneyGoal.toString(),
                        label = stringResource(R.string.money_goal_label),
                        error = editMoneyGoalError,
                        placeholder = currentAccount!!.moneyGoal.toString(),
                        keyboardType = KeyboardType.Number,
                    ) {
                        accountViewModel.onEditAccountMoneyGoalChange(it.toDouble())
                    }

                    DatePickerTextField(
                        modifier = Modifier.width(300.dp),
                        editDateGoal,
                        onDateSelected = {accountViewModel.onEditAccountDateGoalChange(it)},
                        label = stringResource(R.string.date_goal_label),
                        error = editDateGoalError
                    )

                    PhotoTextField(
                        modifier = Modifier.width(300.dp),
                        enabled = true,
                        value = editPhoto ?: "",
                        onValueChange = { accountViewModel.onEditAccountPhotoChange(it) },
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
                            accountViewModel.updateAccountInformation()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(stringResource(R.string.save_btn_text))
                    }
                }
            }
        }

        if (isAddSheetOpen) {

            ModalBottomSheet(
                onDismissRequest = {
                    isAddSheetOpen = false
                },
                sheetState = addSheetState,
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
                        stringResource(R.string.create_new_contribution_title).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                    )

                    FormTextField(
                        value = contribAmount?.toString() ?: "",
                        label = stringResource(R.string.amount_label),
                        error = contributionAmountError,
                        keyboardType = KeyboardType.Number,
                    ) {
                        accountViewModel.onContributionAmountChange(it.toDouble())
                    }


                    Spacer(Modifier.size(8.dp))

                    Button(
                        onClick = {
                            accountViewModel.createNewContribution()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(stringResource(R.string.create_contribution_title))
                    }
                }
            }
        }
    }
}