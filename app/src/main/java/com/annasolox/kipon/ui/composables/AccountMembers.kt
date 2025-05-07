package com.annasolox.kipon.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun AccountMembers(users: Int){
    Row {
        for (i in 0 until users) {
            UserThumbnail(modifier = Modifier
                .size(60.dp)
                .zIndex(i.toFloat())
                .offset(x = (-i * 25).dp))
        }
    }
}