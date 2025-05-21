package com.annasolox.kipon.ui.composables.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.R
import com.annasolox.kipon.ui.models.AccountDetails
import com.annasolox.kipon.ui.models.Saving

@Composable
fun LazyAccountContributions(
    currentBoxSize: Dp,
    contributions: List<Saving>,
    currentAccount: AccountDetails,
    currentAccountAmount: Double,
    listState: LazyListState
) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .offset {
                IntOffset(0, currentBoxSize.roundToPx())
            }
    ) {
        Spacer(Modifier.size(35.dp))

        AccountProgressBar(currentAccountAmount, currentAccount.moneyGoal)

        Spacer(Modifier.size(35.dp))

        if(contributions.isEmpty()){
            Text(stringResource(R.string.no_saving_text))
        }

        LazyColumn(
            state = listState
        ) {
            items(contributions, key = { it.id }) {
                AccountContribution(it)
                HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.Gray)
            }
        }
    }

}