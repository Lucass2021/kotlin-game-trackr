package com.lucasdias.gametrackr.feature.app.community.discover.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityIcon
import com.lucasdias.gametrackr.feature.app.community.components.JoinButton

private val ICON_SIZE = 56.dp
private val NAME_SIZE = 17.sp
private val DETAIL_SIZE = 13.sp

@Composable
fun CommunityRow(
    community: Community,
    onSelect: () -> Unit,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier,
    isOwner: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.lg, vertical = Spacing.sm),
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
            CommunityIcon(start = community.iconStart, end = community.iconEnd, size = ICON_SIZE)
            Column(modifier = Modifier.padding(start = Spacing.sm).weight(1f)) {
                Text(
                    text = community.name,
                    color = AppTextPrimary,
                    style = AppType.headline(NAME_SIZE),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = community.subtitle,
                    color = AppTextSecondary,
                    style = AppType.body(DETAIL_SIZE),
                )
                Text(
                    text = community.description,
                    color = AppTextSecondary,
                    style = AppType.body(DETAIL_SIZE),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        JoinButton(
            isJoined = community.isJoined,
            onClick = onJoin,
            modifier = Modifier.padding(start = Spacing.sm),
            enabled = !isOwner,
        )
    }
}

@Preview
@Composable
private fun CommunityRowPreview() {
    AppPreview {
        CommunityRow(community = CommunityMockData.all.first(), onSelect = {}, onJoin = {})
    }
}
