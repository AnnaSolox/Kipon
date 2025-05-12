package com.annasolox.kipon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.ui.composables.AccountSearchBar
import com.annasolox.kipon.ui.composables.accounts.AccountElevatedCard
import com.annasolox.kipon.ui.composables.images.ImageThumbnail
import com.annasolox.kipon.ui.models.AccountDetails
import com.annasolox.kipon.ui.models.AccountOverview
import com.annasolox.kipon.ui.models.Profile
import com.annasolox.kipon.ui.models.Saving
import com.annasolox.kipon.ui.models.UserHomeScreen
import com.annasolox.kipon.ui.models.UserProfileScreen
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import io.ktor.network.sockets.SocketAddress
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen(userViewModel: UserViewModel = koinViewModel()) {
    //val user by userViewModel.userHome.observeAsState(null)
    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }
    val user = UserHomeScreen(
        userName = "annaSoler",
        photoUrl = "",
        accounts = listOf(
            AccountOverview(1, "Account 1", 100.0, 1000.0),
            AccountOverview(2, "Account 2", 100.0, 1000.0),
            AccountOverview(3, "Account 3", 100.0, 1000.0),
            AccountOverview(4, "Account 4", 100.0, 1000.0),
            AccountOverview(5, "Account 5", 100.0, 1000.0),
            AccountOverview(6, "Account 6", 100.0, 1000.0),
            AccountOverview(7, "Account 7", 100.0, 1000.0),
            AccountOverview(8, "Account 8", 100.0, 1000.0),
            AccountOverview(9, "Account 9", 100.0, 1000.0),
            AccountOverview(10, "Account 10", 100.0, 1000.0),
        )
    )

    val accountDetails: List<AccountDetails> = listOf(
        AccountDetails(
            id = 1,
            name = "Account 1",
            dateGoal = LocalDate.of(2025, 4, 24),
            currentAmount = 1000.0,
            admin = UserProfileScreen(
                id = 1,
                name = "annaSoler",
                email = "annasoler@mail.com",
                profile = Profile(
                    id = 1,
                    completeName = "Anna Soler",
                    telephone = "123456789",
                    address = "Calle Falsa 123",
                    photo = null
                )
            ),
            moneyGoal = 5000.0,
            savings = listOf(
                Saving(
                    id = 1,
                    user = UserProfileScreen(
                        id = 1,
                        name = "annaSoler",
                        email = "annasoler@mail.com",
                        profile = Profile(
                            id = 1,
                            completeName = "Anna Soler",
                            telephone = "123456789",
                            address = "Calle Falsa 123",
                            photo = null
                        )
                    ),
                    amount = 300.0,
                    date = LocalDate.of(2025, 4, 24),
                )
            ),
            userMembers = listOf(
                UserProfileScreen(
                    id = 1,
                    name = "annaSoler",
                    email = "annasoler@mail.com",
                    profile = Profile(
                        id = 1,
                        completeName = "Anna Soler",
                        telephone = "123456789",
                        address = "Calle Falsa 123",
                        photo = null
                    )
                )
            ),
        )
    )

    user.let {
        var accountNames = it.accounts.map { it.name }
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .pointerInput(Unit){
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
                    ImageThumbnail(Modifier.size(60.dp), user.photoUrl)
                    Spacer(Modifier.size(8.dp))
                    Column {
                        Text(
                            "¡Hola ${user.userName}!",
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
                        onClick = {/*TODO*/ },
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(1.dp, Color.Black, shape = CircleShape)
                    ) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add icon")
                    }
                }
            }

            AccountSearchBar(
                user.accounts,
                query,
                onQueryChange = { query = it }
            )

            LazyColumn {
                items(20) {
                    AccountElevatedCard(accountDetails[0], "")
                    Spacer(Modifier.size(8.dp))
                }
            }
        }
    }
}
