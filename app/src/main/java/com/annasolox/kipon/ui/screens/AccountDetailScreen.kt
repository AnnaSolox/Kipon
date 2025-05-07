package com.annasolox.kipon.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.R
import com.annasolox.kipon.ui.composables.AccountMembers
import com.annasolox.kipon.ui.composables.Contribution

@Preview(showBackground = true)
@Composable
fun AccountDetailScreen() {
    val imageHeight: Dp = 240.dp
    val minHeight: Dp = 80.dp

    val scrollState = rememberScrollState()
    val collapseRange = with(LocalDensity.current) { (imageHeight - minHeight).toPx() }

    var scrollOffset by remember { mutableFloatStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = (scrollOffset + delta).coerceIn(-collapseRange, 0f)
                scrollOffset = newOffset
                return Offset.Zero
            }
        }
    }

    val progress = -scrollOffset / collapseRange
    val imageHeightPx = with(LocalDensity.current) { imageHeight.toPx() }

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            Modifier
                .verticalScroll(scrollState)
                .padding(top = imageHeight * (1 - progress))
        ) {
            items(
                20
            ) {
                Contribution()
                Spacer(Modifier.size(24.dp))
            }
        }
    }

    Image(
        painter = painterResource(R.drawable.girl_photo),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(imageHeight - (imageHeight - minHeight) * progress)
            .fillMaxWidth(),
        alignment = Alignment.TopCenter
    )

    Row(
        Modifier
            .fillMaxWidth()
            .height(minHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(style = MaterialTheme.typography.displaySmall, text = "Viaje a Bali")
            Text(style = MaterialTheme.typography.bodyMedium, text = "20/05/2026")
        }
        AccountMembers(3)
    }

}