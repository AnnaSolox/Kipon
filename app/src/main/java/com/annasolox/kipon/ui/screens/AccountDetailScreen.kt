package com.annasolox.kipon.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.annasolox.kipon.R
import com.annasolox.kipon.core.navigation.HomeScreen
import com.annasolox.kipon.ui.composables.accounts.LazyAccountContributions
import com.annasolox.kipon.ui.composables.buttons.OptionsButton
import com.annasolox.kipon.ui.composables.headers.ColumnAccountDetailInfo
import com.annasolox.kipon.ui.composables.textFields.FormTextField
import com.annasolox.kipon.ui.viewmodels.AccountViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(
    navController: NavController,
    accountViewModel: AccountViewModel
) {
    //currentAccount
    val currentAccount by accountViewModel.currentAccount.observeAsState()
    Log.d("AccountDetailScreen", "currentAccount: $currentAccount")

    //Contribution form field
    val contribAmount by accountViewModel.contributionAmount.observeAsState()

    //Nested scroll
    val maxSize = 300.dp
    val minSize = 100.dp
    var currentBoxSize by remember { mutableStateOf(maxSize) }
    var infoImageElementsAlpha by remember { mutableFloatStateOf(1f) }

    //loading state
    val loadingState by accountViewModel.loadingState.observeAsState()

    //navigation
    val navEvent by accountViewModel.navigationEvent.observeAsState()

    val savings by accountViewModel.savingsList.observeAsState()
    val currentAccountAmount by accountViewModel.currentAccountAmount.observeAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(savings) {
        if (!savings.isNullOrEmpty()) {
            listState.animateScrollToItem(0)
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
                                model = currentAccount?.photo,
                                error = painterResource(R.drawable.account_photo),
                                contentDescription = "User image thumbnail",
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
                            OptionsButton()
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
                contentDescription = "Back button",
                modifier = Modifier.size(30.dp),
                tint = Color.White
            )
        }

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val coroutineScope = rememberCoroutineScope()
        var isSheetOpen by remember { mutableStateOf(false) }

        FloatingActionButton(
            onClick = {
                isSheetOpen = true
                coroutineScope.launch { sheetState.show() }
            },
            Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        )
        {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add new contribution icon"
            )
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "Create new contribution".uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                    )

                    FormTextField(
                        value = contribAmount?.toString() ?: "",
                        label = "Amount",
                        error = null,
                    ) {
                        accountViewModel.onContributionAmountChange(it.toDouble())
                    }


                    Spacer(Modifier.size(8.dp))

                    Button(
                        onClick = {
                            accountViewModel.createNewContribution()
                            isSheetOpen = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text("Create contribution")
                    }
                }
            }
        }
    }

}