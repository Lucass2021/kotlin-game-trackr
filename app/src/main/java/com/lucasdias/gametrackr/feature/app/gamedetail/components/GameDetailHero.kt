package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GameDetail
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.FakeGameRepository

private val HERO_HEIGHT = 300.dp
private val FADE_HEIGHT = 140.dp
private val BRAND_SIZE = 96.dp
private val CIRCLE_BUTTON_SIZE = 44.dp
private val CIRCLE_ICON_SIZE = 20.dp
private val BORDER_WIDTH = 1.dp
private const val BRAND_ALPHA = 0.12f
private const val BUTTON_FILL_ALPHA = 0.45f
private const val BUTTON_BORDER_ALPHA = 0.12f

@Composable
fun GameDetailHero(
    game: GameDetail,
    onBack: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var state by remember(game.heroUrl) { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }
    val hasArtwork = game.heroUrl != null && state !is AsyncImagePainter.State.Error
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(HERO_HEIGHT + topInset)
                .then(
                    if (hasArtwork) {
                        Modifier.background(AppSurfaceCard)
                    } else {
                        Modifier.background(Brush.linearGradient(listOf(game.coverStart, game.coverEnd)))
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (!hasArtwork) {
            Icon(
                imageVector = AppIcon.BRAND.image(),
                contentDescription = null,
                tint = Color.White.copy(alpha = BRAND_ALPHA),
                modifier = Modifier.size(BRAND_SIZE),
            )
        }

        game.heroUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                onState = { state = it },
                modifier = Modifier.fillMaxSize(),
            )
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(FADE_HEIGHT)
                    .background(Brush.verticalGradient(listOf(Color.Transparent, AppBackground))),
        )

        Row(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = Spacing.md, vertical = Spacing.xs),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CircleButton(icon = AppIcon.BACK, description = stringResource(R.string.game_detail_back), onClick = onBack)
            CircleButton(icon = AppIcon.SHARE, description = stringResource(R.string.game_detail_share), onClick = onShare)
        }
    }
}

@Composable
private fun CircleButton(
    icon: AppIcon,
    description: String,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(CIRCLE_BUTTON_SIZE)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = BUTTON_FILL_ALPHA))
                .border(BORDER_WIDTH, Color.White.copy(alpha = BUTTON_BORDER_ALPHA), CircleShape)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = description,
            tint = AppTextPrimary,
            modifier = Modifier.size(CIRCLE_ICON_SIZE),
        )
    }
}

@Preview
@Composable
private fun GameDetailHeroPreview() {
    AppPreview {
        GameDetailHero(game = FakeGameRepository.gameDetail, onBack = {}, onShare = {})
    }
}
