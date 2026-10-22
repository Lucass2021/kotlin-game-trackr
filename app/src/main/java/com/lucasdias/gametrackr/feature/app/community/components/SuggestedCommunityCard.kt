package com.lucasdias.gametrackr.feature.app.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData

private val BORDER_WIDTH = 1.dp
private val ICON_SIZE = 48.dp
private val NAME_SIZE = 17.sp
private val DETAIL_SIZE = 13.sp
private const val BORDER_ALPHA = 0.3f

@Composable
fun SuggestedCommunityCard(
    community: Community,
    onSelect: () -> Unit,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Radius.lg)
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppPrimary.copy(alpha = BORDER_ALPHA), shape)
                .padding(Spacing.md),
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
                    text = stringResource(R.string.community_members_count, community.members),
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

        JoinButton(isJoined = community.isJoined, onClick = onJoin, modifier = Modifier.padding(start = Spacing.sm))
    }
}

@Preview
@Composable
private fun SuggestedCommunityCardPreview() {
    AppPreview {
        SuggestedCommunityCard(
            community = CommunityMockData.suggested,
            onSelect = {},
            onJoin = {},
            modifier = Modifier.padding(Spacing.lg),
        )
    }
}
