package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar
import com.lucasdias.gametrackr.feature.app.profile.Profile

private val AvatarSize = 88.dp
private val AvatarOverhang = 44.dp

@Composable
fun ProfileHeader(
    profile: Profile,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Banner(profile)

            Row(
                modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .offset(y = AvatarOverhang)
                        .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                CommunityAvatar(
                    start = profile.avatarStart,
                    end = profile.avatarEnd,
                    size = AvatarSize,
                    modifier = Modifier.border(4.dp, AppBackground, CircleShape),
                )

                Column(modifier = Modifier.padding(bottom = 6.dp)) {
                    Text(
                        text = profile.name,
                        color = AppTextPrimary,
                        style = AppType.headline(24.sp, FontWeight.ExtraBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = profile.username,
                        color = AppPrimary,
                        style = AppType.body(14.sp),
                    )
                }
            }
        }

        Spacer(Modifier.height(AvatarOverhang + 14.dp))

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(text = profile.bio, color = AppTextSecondary, style = AppType.body(15.sp))

            Text(text = profile.joinedAt, color = AppTextSecondary, style = AppType.body(13.sp))

            Actions(onEdit = onEdit, onShare = onShare)
        }
    }
}

@Composable
private fun Banner(profile: Profile) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Brush.linearGradient(listOf(profile.avatarStart, profile.avatarEnd))),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AppIcon.BRAND.image(),
            contentDescription = null,
            tint = AppTextPrimary.copy(alpha = 0.08f),
            modifier = Modifier.size(64.dp),
        )

        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(0.5f to AppBackground.copy(alpha = 0f), 1f to AppBackground),
                    ),
        )
    }
}

@Composable
private fun Actions(
    onEdit: () -> Unit,
    onShare: () -> Unit,
) {
    val editInteraction = remember { MutableInteractionSource() }
    val shareInteraction = remember { MutableInteractionSource() }
    val shareLabel = stringResource(R.string.profile_action_share)

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier =
                Modifier
                    .pressScale(editInteraction)
                    .weight(1f)
                    .height(46.dp)
                    .clip(CircleShape)
                    .background(AppPrimary)
                    .clickable(
                        interactionSource = editInteraction,
                        indication = null,
                        role = Role.Button,
                        onClick = onEdit,
                    ),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = AppIcon.EDIT_PROFILE.image(),
                contentDescription = null,
                tint = AppOnPrimary,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = stringResource(R.string.profile_action_edit),
                color = AppOnPrimary,
                style = AppType.label(15.sp),
            )
        }

        Box(
            modifier =
                Modifier
                    .pressScale(shareInteraction)
                    .size(46.dp)
                    .clip(CircleShape)
                    .border(1.dp, AppOutline, CircleShape)
                    .clickable(
                        interactionSource = shareInteraction,
                        indication = null,
                        onClickLabel = shareLabel,
                        role = Role.Button,
                        onClick = onShare,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.SHARE.image(),
                contentDescription = shareLabel,
                tint = AppTextPrimary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
