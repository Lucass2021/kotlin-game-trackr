package com.lucasdias.gametrackr.feature.app.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard

@Composable
fun GameCoverArt(
    start: Color,
    end: Color,
    modifier: Modifier = Modifier,
    url: String? = null,
    width: Dp? = null,
    height: Dp? = null,
) {
    val shape = RoundedCornerShape(14.dp)
    val sizeModifier = if (width != null && height != null) Modifier.size(width, height) else Modifier
    val brandSize = if (width != null && height != null) minOf(width, height) * 0.32f else 44.dp

    var state by remember(url) { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }
    val hasArtwork = url != null && state !is AsyncImagePainter.State.Error

    Box(
        modifier =
            modifier
                .then(sizeModifier)
                .clip(shape)
                .then(
                    if (hasArtwork) {
                        Modifier.background(AppSurfaceCard)
                    } else {
                        Modifier.background(Brush.linearGradient(listOf(start, end)))
                    },
                ).border(1.dp, AppOutline, shape),
        contentAlignment = Alignment.Center,
    ) {
        if (!hasArtwork) {
            Icon(
                imageVector = AppIcon.BRAND.image(),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.18f),
                modifier = Modifier.size(brandSize),
            )
        }
        if (url != null) {
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                onState = { state = it },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
