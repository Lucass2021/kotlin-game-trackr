package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.GameScreenshot
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.FakeGameRepository
import com.lucasdias.gametrackr.feature.app.home.components.GameCoverArt

private val CLOSE_BUTTON_SIZE = 44.dp
private val CLOSE_ICON_SIZE = 20.dp
private val COUNTER_SIZE = 14.sp
private val ACTIVE_DOT_SIZE = 9.dp
private val DOT_SIZE = 7.dp
private const val SCREENSHOT_RATIO = 16f / 9f
private const val CONTROL_ALPHA = 0.5f
private const val COUNTER_ALPHA = 0.8f
private const val DOT_ALPHA = 0.4f

@Composable
fun GameScreenshotPager(
    screenshots: List<GameScreenshot>,
    startIndex: Int,
    onClose: () -> Unit,
) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false),
    ) {
        GameScreenshotPagerContent(screenshots = screenshots, startIndex = startIndex, onClose = onClose)
    }
}

@Composable
private fun GameScreenshotPagerContent(
    screenshots: List<GameScreenshot>,
    startIndex: Int,
    onClose: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = startIndex) { screenshots.size }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onClose() },
        contentAlignment = Alignment.Center,
    ) {
        HorizontalPager(state = pagerState, pageSpacing = Spacing.sm, modifier = Modifier.fillMaxWidth()) { page ->
            val shot = screenshots[page]
            GameCoverArt(
                start = shot.start,
                end = shot.end,
                url = shot.url,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md)
                        .aspectRatio(SCREENSHOT_RATIO),
            )
        }

        Row(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = Spacing.md, vertical = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(CLOSE_BUTTON_SIZE)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = CONTROL_ALPHA))
                        .clickable(onClick = onClose),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcon.BACK.image(),
                    contentDescription = stringResource(R.string.game_detail_close_screenshots),
                    tint = Color.White,
                    modifier = Modifier.size(CLOSE_ICON_SIZE),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.game_detail_screenshot_counter, pagerState.currentPage + 1, screenshots.size),
                color = Color.White.copy(alpha = COUNTER_ALPHA),
                style = AppType.label(COUNTER_SIZE),
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = CONTROL_ALPHA))
                        .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
            )
        }

        Row(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = Spacing.xxxl),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(screenshots.size) { index ->
                val isActive = index == pagerState.currentPage
                Box(
                    modifier =
                        Modifier
                            .size(if (isActive) ACTIVE_DOT_SIZE else DOT_SIZE)
                            .clip(CircleShape)
                            .background(if (isActive) Color.White else Color.White.copy(alpha = DOT_ALPHA)),
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameScreenshotPagerPreview() {
    AppPreview {
        GameScreenshotPagerContent(screenshots = FakeGameRepository.gameDetail.screenshots, startIndex = 1, onClose = {})
    }
}
