package com.lucasdias.gametrackr.feature.app.community.communitydetail.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.ui.components.AppPreview
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
private val DIVIDER_HEIGHT = 34.dp
private val LIVE_DOT_SIZE = 6.dp
private val VALUE_SIZE = 19.sp
private val LABEL_SIZE = 11.sp
private val LABEL_TRACKING = 0.8.sp

@Composable
fun CommunityStatsBar(
    community: Community,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Radius.lg)
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(BORDER_WIDTH, AppOutline, shape)
                .padding(vertical = Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Stat(value = community.members, label = stringResource(R.string.community_stat_members), modifier = Modifier.weight(1f))
        Divider()
        Stat(value = community.posts, label = stringResource(R.string.community_stat_posts), modifier = Modifier.weight(1f))
        Divider()
        Stat(value = community.online, label = stringResource(R.string.community_stat_online), isLive = true, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun Divider() {
    Box(modifier = Modifier.width(BORDER_WIDTH).height(DIVIDER_HEIGHT).background(AppOutline))
}

@Composable
private fun Stat(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    isLive: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.xxs)) {
            Text(
                text = value,
                color = AppTextPrimary,
                style = AppType.headline(VALUE_SIZE),
            )
            if (isLive) {
                Box(modifier = Modifier.size(LIVE_DOT_SIZE).clip(CircleShape).background(AppPrimary))
            }
        }
        Text(
            text = label,
            color = AppTextSecondary,
            style = AppType.label(LABEL_SIZE),
            letterSpacing = LABEL_TRACKING,
        )
    }
}

@Preview
@Composable
private fun CommunityStatsBarPreview() {
    AppPreview {
        CommunityStatsBar(community = CommunityMockData.detailCommunity)
    }
}
