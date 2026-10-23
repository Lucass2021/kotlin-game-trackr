package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Durations
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.game.FakeGameRepository

private val BODY_SIZE = 15.sp
private val BODY_LINE_HEIGHT = 22.sp
private val TOGGLE_SIZE = 14.sp
private val CARET_SIZE = 12.dp
private val BORDER_WIDTH = 1.dp
private const val COLLAPSED_LINES = 3
private const val EXPANDED_ROTATION = 180f

@Composable
fun GameAboutSection(
    about: String,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val shape = RoundedCornerShape(Radius.lg)

    Column(
        modifier = modifier.padding(horizontal = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        GameSectionHeader(title = stringResource(R.string.game_detail_about))

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(AppSurfaceCard)
                    .border(BORDER_WIDTH, AppOutline, shape)
                    .padding(Spacing.md)
                    .animateContentSize(tween(Durations.SHORT)),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Text(
                text = about,
                color = AppTextSecondary,
                style = AppType.body(BODY_SIZE),
                lineHeight = BODY_LINE_HEIGHT,
                maxLines = if (expanded) Int.MAX_VALUE else COLLAPSED_LINES,
                overflow = TextOverflow.Ellipsis,
            )

            Row(
                modifier = Modifier.clickable(role = Role.Button) { expanded = !expanded },
                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(if (expanded) R.string.game_detail_read_less else R.string.game_detail_read_more),
                    color = AppSecondary,
                    style = AppType.label(TOGGLE_SIZE),
                )
                Icon(
                    imageVector = AppIcon.CARET_DOWN.image(),
                    contentDescription = null,
                    tint = AppSecondary,
                    modifier = Modifier.size(CARET_SIZE).rotate(if (expanded) EXPANDED_ROTATION else 0f),
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameAboutSectionPreview() {
    AppPreview {
        GameAboutSection(about = FakeGameRepository.gameDetail.about)
    }
}
