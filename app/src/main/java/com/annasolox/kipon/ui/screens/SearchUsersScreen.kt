package com.annasolox.kipon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.annasolox.kipon.R
import com.annasolox.kipon.core.navigation.DetailsAccountScreen
import com.annasolox.kipon.core.navigation.SearchUsersScreen
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
        val fetchedUsersError by userViewModel.fetchedUsersError.observeAsState()
        val loading by userViewModel.loading.observeAsState()

        var query by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            accountViewModel.onUserAdded.collect {
                navController.navigate(DetailsAccountScreen)
            }
        }

        Column {

            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                },
                label = { Text(stringResource(R.string.search_label)) },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(40.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if(query.isNotBlank()){
                            coroutineScope.launch {
                                userViewModel.searchUsers(query)
                            }
                        } else {
                            userViewModel.clearFetchedUsers()
                        }
                    }
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if(query.isNotBlank()){
                                coroutineScope.launch {
                                    userViewModel.searchUsers(query)
                                }
                            } else {
                                userViewModel.clearFetchedUsers()
                            }
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = stringResource(R.string.cd_trailing_icon)
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
                            navController.navigate(DetailsAccountScreen){
                                launchSingleTop = true
                                popUpTo(SearchUsersScreen){ inclusive = true}
                            }
                        })
                        HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.Gray)
                    }
                }

                if (fetchedUsers!!.isEmpty()){
                    Text(stringResource(R.string.search_user_instruction))
                }
            } ?: Text(stringResource(R.string.search_user_instruction))

            if(fetchedUsersError != null){
                Text(stringResource(R.string.search_user_error))
            }

            if(loading == true){
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }
        }
    }
}