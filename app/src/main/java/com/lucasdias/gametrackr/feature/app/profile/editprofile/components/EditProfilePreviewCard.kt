package com.lucasdias.gametrackr.feature.app.profile.editprofile.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Durations
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.core.ui.theme.darkened
import com.lucasdias.gametrackr.core.ui.theme.toAppColor
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar

private val AVATAR_SIZE = 64.dp
private val NAME_SIZE = 18.sp
private val USERNAME_SIZE = 14.sp
private const val AVATAR_GRADIENT_DEPTH = 0.28f

@Composable
fun EditProfilePreviewCard(
    name: String,
    username: String,
    avatarHex: String,
    modifier: Modifier = Modifier,
) {
    val start by animateColorAsState(avatarHex.toAppColor(), tween(Durations.SHORT), label = "avatarStart")
    val end by animateColorAsState(
        avatarHex.toAppColor().darkened(AVATAR_GRADIENT_DEPTH),
        tween(Durations.SHORT),
        label = "avatarEnd",
    )

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Radius.lg))
                .background(AppSurfaceCard)
                .padding(Spacing.md),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CommunityAvatar(start = start, end = end, size = AVATAR_SIZE)

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xxxs)) {
            Text(
                text = name,
                color = AppTextPrimary,
                style = AppType.headline(NAME_SIZE),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = username,
                color = AppPrimary,
                style = AppType.body(USERNAME_SIZE),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun EditProfilePreviewCardPreview() {
    AppPreview {
        EditProfilePreviewCard(
            name = ProfileMockData.profile.name,
            username = ProfileMockData.profile.username,
            avatarHex = ProfileMockData.profile.avatarHex,
        )
    }
}
