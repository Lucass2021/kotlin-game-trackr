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
import com.lucasdias.gametrackr.feature.app.profile.ProfileHeaderMode

private val AvatarSize = 88.dp
private val AvatarOverhang = 44.dp

@Composable
fun ProfileHeader(
    profile: Profile,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
    mode: ProfileHeaderMode = ProfileHeaderMode.Own,
    onEdit: () -> Unit = {},
    onAddFriend: () -> Unit = {},
    onMessage: () -> Unit = {},
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

            Actions(
                mode = mode,
                onEdit = onEdit,
                onShare = onShare,
                onAddFriend = onAddFriend,
                onMessage = onMessage,
            )
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
    mode: ProfileHeaderMode,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    onAddFriend: () -> Unit,
    onMessage: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        when (mode) {
            ProfileHeaderMode.Own -> {
                PrimaryPill(
                    icon = AppIcon.EDIT_PROFILE,
                    text = stringResource(R.string.profile_action_edit),
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                )
                CircleAction(
                    icon = AppIcon.SHARE,
                    label = stringResource(R.string.profile_action_share),
                    onClick = onShare,
                )
            }

            is ProfileHeaderMode.Other -> {
                if (mode.isFriend) {
                    SecondaryPill(
                        icon = AppIcon.CHECK,
                        text = stringResource(R.string.profile_action_friends),
                        onClick = onAddFriend,
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    PrimaryPill(
                        icon = AppIcon.ADD_FRIEND,
                        text = stringResource(R.string.profile_action_add_friend),
                        onClick = onAddFriend,
                        modifier = Modifier.weight(1f),
                    )
                }
                CircleAction(
                    icon = AppIcon.ENVELOPE,
                    label = stringResource(R.string.profile_action_message),
                    onClick = onMessage,
                )
                CircleAction(
                    icon = AppIcon.SHARE,
                    label = stringResource(R.string.profile_action_share),
                    onClick = onShare,
                )
            }
        }
    }
}

@Composable
private fun PrimaryPill(
    icon: AppIcon,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interaction = remember { MutableInteractionSource() }
    Row(
        modifier =
            modifier
                .pressScale(interaction)
                .height(46.dp)
                .clip(CircleShape)
                .background(AppPrimary)
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClickLabel = text,
                    role = Role.Button,
                    onClick = onClick,
                ),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = null,
            tint = AppOnPrimary,
            modifier = Modifier.size(18.dp),
        )
        Text(text = text, color = AppOnPrimary, style = AppType.label(15.sp))
    }
}

@Composable
private fun SecondaryPill(
    icon: AppIcon,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interaction = remember { MutableInteractionSource() }
    Row(
        modifier =
            modifier
                .pressScale(interaction)
                .height(46.dp)
                .clip(CircleShape)
                .border(1.dp, AppOutline, CircleShape)
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClickLabel = text,
                    role = Role.Button,
                    onClick = onClick,
                ),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = null,
            tint = AppTextPrimary,
            modifier = Modifier.size(18.dp),
        )
        Text(text = text, color = AppTextPrimary, style = AppType.label(15.sp))
    }
}

@Composable
private fun CircleAction(
    icon: AppIcon,
    label: String,
    onClick: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier =
            Modifier
                .pressScale(interaction)
                .size(46.dp)
                .clip(CircleShape)
                .border(1.dp, AppOutline, CircleShape)
                .clickable(
                    interactionSource = interaction,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon.image(),
            contentDescription = label,
            tint = AppTextPrimary,
            modifier = Modifier.size(20.dp),
        )
    }
}
