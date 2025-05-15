package com.annasolox.kipon.ui.composables.headers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.data.api.models.response.UserSimplified
import com.annasolox.kipon.ui.composables.accounts.AccountMembers

@Composable
fun ColumnAccountDetailInfo(members: List<UserSimplified>, title: String, date: String) {

    AccountMembers(Modifier.size(50.dp),members)
    Spacer(Modifier.size(8.dp))
    Text(
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Black,
        color = Color.White,
        text = title
    )
    Spacer(Modifier.size(4.dp))
    Text(
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Black,
        color = Color.White,
        text = date
    )
}