package com.annasolox.kipon.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.R

@Preview
@Composable
fun AccountElevatedCard(users: Int = 3) {
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.account_photo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(.25f),
                contentScale = ContentScale.Crop,
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(.75f)
                    .padding(start = 16.dp, end = 16.dp),
            ) {
                Spacer(Modifier.size(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        text = "Título de la hucha"
                    )
                    Text(style = MaterialTheme.typography.bodyMedium, text = "20/05/2025")
                }

                Spacer(Modifier.size(16.dp))

                AccountMembers(users)

                Spacer(Modifier.size(24.dp))

                AccountProgressBar(6000, 12000)
            }
        }
    }
}