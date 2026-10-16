package com.lucasdias.gametrackr.feature.app.profile.setup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.data.profile.ProfileMockData

@Composable
fun SetupCover(
    setup: SetupItem,
    modifier: Modifier = Modifier,
    cornerRadius: Int = 14,
    placeholderSize: Int = 38,
) {
    val shape = RoundedCornerShape(cornerRadius.dp)

    Box(
        modifier =
            modifier
                .clip(shape)
                .background(Brush.linearGradient(listOf(setup.palette.start, setup.palette.end)))
                .border(1.dp, AppOutline, shape),
        contentAlignment = Alignment.Center,
    ) {
        val cover = setup.images.firstOrNull()

        if (cover == null) {
            Icon(
                imageVector = AppIcon.DEVICES.image(),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.22f),
                modifier = Modifier.size(placeholderSize.dp),
            )
        } else {
            AsyncImage(
                model = cover.url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (setup.images.size > 1) {
            Row(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.55f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = AppIcon.GRID.image(),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(11.dp),
                )
                Text(
                    text = "${setup.images.size}",
                    color = Color.White,
                    style = AppType.label(11.sp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun SetupCoverPreview() {
    AppPreview {
        SetupCover(setup = ProfileMockData.setups[1], modifier = Modifier.padding(20.dp).size(width = 200.dp, height = 130.dp))
    }
}
