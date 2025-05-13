package com.annasolox.kipon.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.annasolox.kipon.R
import com.annasolox.kipon.core.navigation.AccountNavigationEvent
import com.annasolox.kipon.core.navigation.AccountNavigationEvent.*
import com.annasolox.kipon.core.navigation.BottomNavscreen
import com.annasolox.kipon.core.navigation.DetailsAccountScreen
import com.annasolox.kipon.core.navigation.LoginNavigationEvent.NavigateToHome
import com.annasolox.kipon.core.navigation.LoginScreen
import com.annasolox.kipon.ui.composables.accounts.LazyAccountContributions
import com.annasolox.kipon.ui.composables.buttons.OptionsButton
import com.annasolox.kipon.ui.composables.headers.ColumnAccountDetailInfo
import com.annasolox.kipon.ui.composables.headers.ImageHeader
import com.annasolox.kipon.ui.models.LoginUiState
import com.annasolox.kipon.ui.viewmodels.AccountViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountDetailScreen(
    navController: NavController,
    accountViewModel: AccountViewModel = koinViewModel()
) {
    //currentAccount
    val currentAccount by accountViewModel.currentAccount.observeAsState()
    Log.d("AccountDetailScreen", "currentAccount: $currentAccount")

    //Nested scroll
    val maxSize = 300.dp
    val minSize = 100.dp
    var currentBoxSize by remember { mutableStateOf(maxSize) }
    var infoImageElementsAlpha by remember { mutableFloatStateOf(1f) }

    //loading state
    val loadingState by accountViewModel.loadingState.observeAsState()

    //navigation
    val navEvent by accountViewModel.navigationEvent.observeAsState()

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

    currentAccount?.let{
        Box(Modifier.nestedScroll(nestedScrollConnection)) {

            LazyAccountContributions(currentBoxSize, currentAccount!!.photo ?: "")

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
                ImageHeader(
                    height = currentBoxSize,
                    imageResource = R.drawable.account_photo,
                    contentImageDescription = "Account image"
                )

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
                            currentAccount!!.dateGoal,
                            currentAccount!!.photo ?: ""
                        )
                    }

                    OptionsButton()
                }
            }
        }
    } ?: run {
        CircularProgressIndicator()
    }


}