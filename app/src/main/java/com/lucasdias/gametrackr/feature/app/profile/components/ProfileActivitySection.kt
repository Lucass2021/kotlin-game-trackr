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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.profile.ActivityEvent

@Composable
fun ProfileActivitySection(
    events: List<ActivityEvent>,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(vertical = 4.dp),
    ) {
        events.forEachIndexed { index, event ->
            ActivityRow(event)

            if (index < events.lastIndex) {
                Box(
                    modifier =
                        Modifier
                            .padding(start = 52.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(AppOutline),
                )
            }
        }
    }
}

@Composable
private fun ActivityRow(event: ActivityEvent) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(CircleShape).background(AppBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = event.kind.icon.image(),
                contentDescription = null,
                tint = event.kind.tint,
                modifier = Modifier.size(18.dp),
            )
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = stringResource(event.kind.verbRes),
                color = AppTextSecondary,
                style = AppType.body(13.sp),
            )
            Text(
                text = event.detail?.let { "${event.gameTitle} · $it" } ?: event.gameTitle,
                color = AppTextPrimary,
                style = AppType.label(15.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Text(text = event.timeAgo, color = AppTextSecondary, style = AppType.body(13.sp))
    }
}
