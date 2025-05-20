package com.annasolox.kipon.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.annasolox.kipon.core.navigation.DetailsAccountScreen
import com.annasolox.kipon.ui.composables.UserSearchComposable
import com.annasolox.kipon.ui.viewmodels.AccountViewModel
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchUsersScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    accountViewModel: AccountViewModel
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.TopCenter
    ) {

        val fetchedUsers by userViewModel.fetchedUsers.observeAsState()
        var query by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()

        Column {

            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                },
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(40.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                userViewModel.searchUsers(query)
                            }
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = "Trailing icon"
                        )
                    }
                },
            )

            fetchedUsers?.let {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                ) {
                    items(fetchedUsers!!.size) { index ->
                        val user = fetchedUsers!!.get(index)
                        UserSearchComposable(user = user, modifier = Modifier.clickable {
                            accountViewModel.addUserToAccount(user.id)
                            navController.navigate(DetailsAccountScreen)
                        })
                        HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.Gray)
                    }
                }
            }
        }
    }
}