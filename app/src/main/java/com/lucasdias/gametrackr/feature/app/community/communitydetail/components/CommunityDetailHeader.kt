package com.lucasdias.gametrackr.feature.app.community.communitydetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.community.CommunityMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityIcon

private val BANNER_HEIGHT = 180.dp
private val BANNER_ICON_SIZE = 72.dp
private const val BANNER_ICON_ALPHA = 0.08f
private const val BANNER_FADE_START = 0.4f
private val ICON_SIZE = 80.dp
private val ICON_OVERHANG = 40.dp
private val ICON_BORDER_WIDTH = 3.dp
private val NAME_SIZE = 24.sp
private val SUBTITLE_SIZE = 13.sp

@Composable
fun CommunityDetailHeader(
    community: Community,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(BANNER_HEIGHT)
                    .background(Brush.linearGradient(listOf(community.iconStart, community.iconEnd))),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(BANNER_HEIGHT)
                        .background(
                            Brush.verticalGradient(
                                BANNER_FADE_START to Color.Transparent,
                                1f to AppBackground,
                            ),
                        ),
            )
            Icon(
                imageVector = AppIcon.COMMUNITY.image(),
                contentDescription = null,
                tint = AppTextPrimary.copy(alpha = BANNER_ICON_ALPHA),
                modifier = Modifier.size(BANNER_ICON_SIZE),
            )
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.lg)
                    .offset(y = -ICON_OVERHANG),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CommunityIcon(
                start = community.iconStart,
                end = community.iconEnd,
                size = ICON_SIZE,
                cornerRadius = Radius.lg,
                modifier = Modifier.border(ICON_BORDER_WIDTH, AppBackground, RoundedCornerShape(Radius.lg)),
            )
            Column(modifier = Modifier.padding(start = Spacing.sm).weight(1f)) {
                Text(
                    text = community.name,
                    color = AppTextPrimary,
                    style = AppType.headline(NAME_SIZE, FontWeight.ExtraBold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = community.detailSubtitle,
                    color = AppTextSecondary,
                    style = AppType.body(SUBTITLE_SIZE),
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommunityDetailHeaderPreview() {
    AppPreview {
        CommunityDetailHeader(community = CommunityMockData.detailCommunity)
    }
}
