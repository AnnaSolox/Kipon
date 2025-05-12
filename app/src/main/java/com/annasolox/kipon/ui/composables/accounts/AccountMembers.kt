package com.annasolox.kipon.ui.composables.accounts

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.annasolox.kipon.R
import com.annasolox.kipon.ui.composables.images.ImageThumbnail

@Composable
fun AccountMembers(modifier: Modifier, users: Int, imageUrl: String){
    Row {
        for (i in 0 until users) {
            ImageThumbnail(
                imageUrl = imageUrl,
                modifier = modifier
                    .zIndex(i.toFloat())
                    .offset(x = (-i * 25).dp),
            )
        }
    }
}