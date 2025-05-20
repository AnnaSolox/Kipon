package com.annasolox.kipon.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.ui.composables.images.ImageThumbnail
import com.annasolox.kipon.ui.models.SearchedUser

@Composable
fun UserSearchComposable(user: SearchedUser) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageThumbnail(Modifier.size(55.dp), imageUrl = user.photo)

        Spacer(Modifier.size(20.dp))

        Text(style = MaterialTheme.typography.bodyLarge, text = user.userName)
    }
}