package com.lucasdias.gametrackr.feature.app.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.BackButton
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val ICON_SIZE = 22.dp
private val TITLE_SIZE = 22.sp
private val SUBTITLE_SIZE = 13.sp

@Composable
fun StatsTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onShare: (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shareLabel = stringResource(R.string.stats_share)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(AppBackground)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BackButton(onBack = onBack)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = AppTextPrimary,
                style = AppType.headline(TITLE_SIZE),
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = AppTextSecondary,
                    style = AppType.body(SUBTITLE_SIZE),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        if (onShare != null) {
            Icon(
                imageVector = AppIcon.SHARE.image(),
                contentDescription = shareLabel,
                tint = AppSecondary,
                modifier =
                    Modifier
                        .pressScale(interactionSource)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClickLabel = shareLabel,
                            role = Role.Button,
                            onClick = onShare,
                        ).size(ICON_SIZE),
            )
        }
    }
}

@Preview
@Composable
private fun StatsTopBarPreview() {
    AppPreview {
        StatsTopBar(title = "Stats", subtitle = "Ana", onBack = {}, onShare = {})
    }
}
