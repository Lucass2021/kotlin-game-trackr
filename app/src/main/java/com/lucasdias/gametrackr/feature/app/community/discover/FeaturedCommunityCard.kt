package com.lucasdias.gametrackr.feature.app.community.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.Community
import com.lucasdias.gametrackr.feature.app.community.components.CommunityIcon
import com.lucasdias.gametrackr.feature.app.community.components.JoinButton

@Composable
fun FeaturedCommunityCard(
    community: Community,
    onSelect: () -> Unit,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier =
            modifier
                .width(210.dp)
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape),
    ) {
        Column(
            modifier =
                Modifier
                    .pressScale(interactionSource)
                    .clickable(interactionSource = interactionSource, indication = null, onClick = onSelect),
        ) {
            Box {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .background(Brush.linearGradient(listOf(community.iconStart, community.iconEnd))),
                )
                CommunityIcon(
                    start = community.iconStart,
                    end = community.iconEnd,
                    size = 48.dp,
                    cornerRadius = 12.dp,
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 14.dp)
                            .offset(y = 22.dp)
                            .border(3.dp, AppSurfaceCard, RoundedCornerShape(12.dp)),
                )
            }

            Column(modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 26.dp)) {
                Text(
                    text = community.name,
                    color = AppTextPrimary,
                    style = AppType.headline(17.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${community.members} members",
                    color = AppTextSecondary,
                    style = AppType.body(13.sp),
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            JoinButton(isJoined = community.isJoined, onClick = onJoin)
        }
    }
}
