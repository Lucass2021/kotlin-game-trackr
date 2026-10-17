package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar
import com.lucasdias.gametrackr.feature.app.profile.ProfileHeaderMode
import java.time.format.DateTimeFormatter
import java.util.Locale

private val AVATAR_SIZE = 88.dp
private val AVATAR_OVERHANG = 44.dp
private val AVATAR_BORDER_WIDTH = 4.dp
private val BANNER_HEIGHT = 150.dp
private val BANNER_ICON_SIZE = 64.dp
private const val BANNER_ICON_ALPHA = 0.08f
private const val BANNER_FADE_START = 0.5f
private val NAME_SIZE = 24.sp
private val USERNAME_SIZE = 14.sp
private val BIO_SIZE = 15.sp
private val JOINED_SIZE = 13.sp
private val JOINED_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)

@Composable
fun ProfileHeader(
    profile: Profile,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
    mode: ProfileHeaderMode = ProfileHeaderMode.Own,
    isGuest: Boolean = false,
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
                        .offset(y = AVATAR_OVERHANG)
                        .padding(horizontal = Spacing.lg),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                CommunityAvatar(
                    start = profile.avatarStart,
                    end = profile.avatarEnd,
                    size = AVATAR_SIZE,
                    modifier = Modifier.border(AVATAR_BORDER_WIDTH, AppBackground, CircleShape),
                )

                Column(modifier = Modifier.padding(bottom = Spacing.xxs)) {
                    Text(
                        text = profile.name,
                        color = AppTextPrimary,
                        style = AppType.headline(NAME_SIZE, FontWeight.ExtraBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = profile.username,
                        color = AppPrimary,
                        style = AppType.body(USERNAME_SIZE),
                    )
                }
            }
        }

        Spacer(Modifier.height(AVATAR_OVERHANG + Spacing.sm))

        Column(
            modifier = Modifier.padding(horizontal = Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            if (profile.bio.isNotBlank()) {
                Text(text = profile.bio, color = AppTextSecondary, style = AppType.body(BIO_SIZE))
            }

            profile.joinedAt?.let { joinedAt ->
                Text(
                    text = stringResource(R.string.profile_joined, JOINED_FORMATTER.format(joinedAt)),
                    color = AppTextSecondary,
                    style = AppType.body(JOINED_SIZE),
                )
            }

            ProfileHeaderActions(
                mode = mode,
                isGuest = isGuest,
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
                .height(BANNER_HEIGHT)
                .background(Brush.linearGradient(listOf(profile.avatarStart, profile.avatarEnd))),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AppIcon.BRAND.image(),
            contentDescription = null,
            tint = AppTextPrimary.copy(alpha = BANNER_ICON_ALPHA),
            modifier = Modifier.size(BANNER_ICON_SIZE),
        )

        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            BANNER_FADE_START to AppBackground.copy(alpha = 0f),
                            1f to AppBackground,
                        ),
                    ),
        )
    }
}

@Preview
@Composable
private fun ProfileHeaderPreview() {
    AppPreview {
        ProfileHeader(profile = ProfileMockData.profile, onShare = {})
    }
}
