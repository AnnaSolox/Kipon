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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.ui.composables.images.ImageThumbnail

@Composable
fun Contribution(imageResource: Int) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        ImageThumbnail(Modifier.size(55.dp), imageResource)

        Spacer(Modifier.size(20.dp))

        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(style = MaterialTheme.typography.bodyMedium, text = "Clara Moreno García")
                Text(style = MaterialTheme.typography.titleSmall, text = "+500€")
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(style = MaterialTheme.typography.bodySmall, text = "24/04/2025")
                Text(style = MaterialTheme.typography.bodySmall, text = "6000€")
            }
        }
    }
}
