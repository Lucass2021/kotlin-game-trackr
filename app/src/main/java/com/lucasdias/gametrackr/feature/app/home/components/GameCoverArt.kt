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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart
import com.lucasdias.gametrackr.core.ui.theme.Radius

private val BORDER_WIDTH = 1.dp
private val BRAND_SIZE = 44.dp
private const val BRAND_RATIO = 0.32f
private const val BRAND_ALPHA = 0.18f
private val PREVIEW_WIDTH = 150.dp
private val PREVIEW_HEIGHT = 200.dp

@Composable
fun GameCoverArt(
    start: Color,
    end: Color,
    modifier: Modifier = Modifier,
    url: String? = null,
    width: Dp? = null,
    height: Dp? = null,
) {
    val shape = RoundedCornerShape(Radius.md)
    val sizeModifier = if (width != null && height != null) Modifier.size(width, height) else Modifier
    val brandSize = if (width != null && height != null) minOf(width, height) * BRAND_RATIO else BRAND_SIZE

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
                ).border(BORDER_WIDTH, AppOutline, shape),
        contentAlignment = Alignment.Center,
    ) {
        if (!hasArtwork) {
            Icon(
                imageVector = AppIcon.BRAND.image(),
                contentDescription = null,
                tint = Color.White.copy(alpha = BRAND_ALPHA),
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

@Preview
@Composable
private fun GameCoverArtPreview() {
    AppPreview {
        GameCoverArt(start = CoverVioletStart, end = CoverVioletEnd, width = PREVIEW_WIDTH, height = PREVIEW_HEIGHT)
    }
}
