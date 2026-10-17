package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.ActivityEvent
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.profile.ProfileMockData

private val BORDER_WIDTH = 1.dp
private val DIVIDER_INSET = 52.dp
private val ICON_BOX_SIZE = 36.dp
private val ICON_SIZE = 18.dp
private val TITLE_SIZE = 15.sp
private val DETAIL_SIZE = 13.sp

@Composable
fun ProfileActivitySection(
    events: List<ActivityEvent>,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Radius.lg)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .padding(vertical = Spacing.xxs),
    ) {
        events.forEachIndexed { index, event ->
            ActivityRow(event)

            if (index < events.lastIndex) {
                Box(
                    modifier =
                        Modifier
                            .padding(start = DIVIDER_INSET)
                            .fillMaxWidth()
                            .height(BORDER_WIDTH)
                            .background(AppOutline),
                )
            }
        }
    }
}

@Composable
private fun ActivityRow(event: ActivityEvent) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.sm, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Box(
            modifier = Modifier.size(ICON_BOX_SIZE).clip(CircleShape).background(AppBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = event.kind.icon.image(),
                contentDescription = null,
                tint = event.kind.tint,
                modifier = Modifier.size(ICON_SIZE),
            )
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)) {
            Text(
                text = stringResource(event.kind.verbRes),
                color = AppTextSecondary,
                style = AppType.body(DETAIL_SIZE),
            )
            Text(
                text = event.detail?.let { "${event.gameTitle} · $it" } ?: event.gameTitle,
                color = AppTextPrimary,
                style = AppType.label(TITLE_SIZE),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Text(text = event.timeAgo, color = AppTextSecondary, style = AppType.body(DETAIL_SIZE))
    }
}

@Preview
@Composable
private fun ProfileActivitySectionPreview() {
    AppPreview {
        ProfileActivitySection(events = ProfileMockData.activity)
    }
}
