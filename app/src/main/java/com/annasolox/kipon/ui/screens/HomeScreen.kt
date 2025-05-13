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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.livedata.observeAsState
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
import com.annasolox.kipon.ui.composables.UserSearchBar
import com.annasolox.kipon.ui.composables.accounts.AccountElevatedCard
import com.annasolox.kipon.ui.composables.images.ImageThumbnail
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen(userViewModel: UserViewModel = koinViewModel()) {

    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }
    val user by userViewModel.userHome.observeAsState()

    user?.let {
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
                query,
                onQueryChange = { query = it }
            )

            LazyColumn {
                val filteredAccounts = user!!.accounts.filter {
                    it.name.contains(query, ignoreCase = true)
                }
                items (filteredAccounts) { account ->
                    AccountElevatedCard(account, "")
                    Spacer(Modifier.size(8.dp))
                }
            }
        }
    }
}
