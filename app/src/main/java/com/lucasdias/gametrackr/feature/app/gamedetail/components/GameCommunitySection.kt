package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.gamedetail.GameDiscussion

@Composable
fun GameCommunitySection(
    discussions: List<GameDiscussion>,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        GameSectionHeader(
            title = stringResource(R.string.game_detail_community),
            actionText = stringResource(R.string.game_detail_see_all),
            onAction = onSeeAll,
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            discussions.forEach { DiscussionCard(it) }
        }
    }
}

@Composable
private fun DiscussionCard(discussion: GameDiscussion) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape),
    ) {
        Box(modifier = Modifier.width(3.dp).fillMaxHeight().background(AppPrimary))

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier =
                        Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(discussion.avatarStart, discussion.avatarEnd))),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = AppIcon.AVATAR.image(),
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp),
                    )
                }
                Text(
                    text = discussion.author,
                    color = AppPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 10.dp).weight(1f),
                )
                Text(
                    text = discussion.timeAgo,
                    color = AppTextSecondary,
                    fontSize = 12.sp,
                )
            }

            Text(
                text = discussion.title,
                color = AppTextPrimary,
                style = AppType.headline(17.sp),
            )

            Text(
                text = discussion.preview,
                color = AppTextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                Metric(icon = AppIcon.COMMUNITY, value = discussion.comments)
                Metric(icon = AppIcon.LIKE, value = discussion.likes)
            }
        }
    }
}

@Composable
private fun Metric(
    icon: AppIcon,
    value: Int,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = null,
            tint = AppTextSecondary,
            modifier = Modifier.size(15.dp),
        )
        Text(text = value.toString(), color = AppTextSecondary, fontSize = 13.sp)
    }
}
