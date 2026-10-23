package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val BACK_BUTTON_SIZE = 44.dp
private val BACK_ICON_SIZE = 20.dp

@Composable
fun GameDetailPlaceholder(
    hasError: Boolean,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        if (hasError) {
            EmptyState(
                icon = AppIcon.INFO,
                title = stringResource(R.string.game_detail_load_error_title),
                message = stringResource(R.string.game_detail_load_error_message),
                actionTitle = stringResource(R.string.game_detail_retry),
                onAction = onRetry,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            CircularProgressIndicator(color = AppPrimary, modifier = Modifier.align(Alignment.Center))
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(horizontal = Spacing.md, vertical = Spacing.xs)
                    .size(BACK_BUTTON_SIZE)
                    .clip(CircleShape)
                    .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.BACK.image(),
                contentDescription = stringResource(R.string.game_detail_back),
                tint = AppTextPrimary,
                modifier = Modifier.size(BACK_ICON_SIZE),
            )
        }
    }
}

@Preview
@Composable
private fun GameDetailPlaceholderLoadingPreview() {
    AppPreview {
        GameDetailPlaceholder(hasError = false, onBack = {}, onRetry = {})
    }
}

@Preview
@Composable
private fun GameDetailPlaceholderErrorPreview() {
    AppPreview {
        GameDetailPlaceholder(hasError = true, onBack = {}, onRetry = {})
    }
}
