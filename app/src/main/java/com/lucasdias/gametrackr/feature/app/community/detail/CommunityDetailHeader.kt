package com.lucasdias.gametrackr.feature.app.community.detail

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.app.community.components.CommunityIcon

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
                    .height(180.dp)
                    .background(Brush.linearGradient(listOf(community.iconStart, community.iconEnd))),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            Brush.verticalGradient(
                                0.4f to Color.Transparent,
                                1f to AppBackground,
                            ),
                        ),
            )
            Icon(
                imageVector = AppIcon.COMMUNITY.image(),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.08f),
                modifier = Modifier.size(72.dp),
            )
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-40).dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CommunityIcon(
                start = community.iconStart,
                end = community.iconEnd,
                size = 80.dp,
                cornerRadius = 18.dp,
                modifier = Modifier.border(3.dp, AppBackground, RoundedCornerShape(18.dp)),
            )
            Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                Text(
                    text = community.name,
                    color = AppTextPrimary,
                    style = AppType.headline(24.sp, FontWeight.ExtraBold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = community.detailSubtitle,
                    color = AppTextSecondary,
                    style = AppType.body(13.sp),
                )
            }
        }
    }
}
