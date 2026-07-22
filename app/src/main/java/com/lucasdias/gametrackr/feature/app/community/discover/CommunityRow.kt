package com.lucasdias.gametrackr.feature.app.community.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.app.community.components.CommunityIcon
import com.lucasdias.gametrackr.feature.app.community.components.JoinButton

@Composable
fun CommunityRow(
    community: Community,
    onSelect: () -> Unit,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier =
                Modifier
                    .weight(1f)
                    .pressScale(interactionSource)
                    .clickable(interactionSource = interactionSource, indication = null, onClick = onSelect),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CommunityIcon(start = community.iconStart, end = community.iconEnd, size = 56.dp)
            Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                Text(
                    text = community.name,
                    color = AppTextPrimary,
                    style = AppType.headline(17.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = community.subtitle,
                    color = AppTextSecondary,
                    style = AppType.body(13.sp),
                )
                Text(
                    text = community.description,
                    color = AppTextSecondary,
                    style = AppType.body(13.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        JoinButton(isJoined = community.isJoined, onClick = onJoin, modifier = Modifier.padding(start = 12.dp))
    }
}
