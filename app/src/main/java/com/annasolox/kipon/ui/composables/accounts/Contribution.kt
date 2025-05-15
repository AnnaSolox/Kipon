package com.annasolox.kipon.ui.composables.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.ui.composables.images.ImageThumbnail
import com.annasolox.kipon.ui.models.Saving

@Composable
fun Contribution(
    saving: Saving
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageThumbnail(Modifier.size(55.dp), imageUrl = saving.photo)

        Spacer(Modifier.size(20.dp))

        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(style = MaterialTheme.typography.bodyMedium, text = saving.user)
                Text(style = MaterialTheme.typography.titleSmall, text = "+${saving.amount}€",
                    fontWeight = FontWeight.Black)
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(style = MaterialTheme.typography.bodySmall, text = saving.date)
                Text(style = MaterialTheme.typography.bodySmall, text = "${saving.currentMoney}€")
            }
        }
    }
}
