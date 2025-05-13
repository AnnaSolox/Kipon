package com.annasolox.kipon.ui.composables.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.annasolox.kipon.R
import com.annasolox.kipon.ui.models.AccountOverview

@Composable
fun AccountElevatedCard(accountOverview: AccountOverview, imageUrl: String, modifier: Modifier) {

    ElevatedCard(
        modifier
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(150.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                error = painterResource(R.drawable.account_photo),
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
                Column(Modifier.weight(.7f).padding(top = 16.dp)) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        text = accountOverview.name
                    )
                    Spacer(Modifier.size(6.dp))
                    Text(style = MaterialTheme.typography.bodyMedium, text = accountOverview.dateGoal.toString())
                }

                Column(Modifier.weight(.3f)) {
                    AccountProgressBar(accountOverview.currentMoney, accountOverview.moneyGoal)
                }
            }
        }
    }
}