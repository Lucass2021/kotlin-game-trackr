package com.lucasdias.gametrackr.feature.app.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.shareText
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.library.components.LibraryEntryRow
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileActivitySection
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileBreakdownSection
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileFavoritesRow
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileHeader
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileStatsBar

@Composable
fun UserProfileScreen(
    user: UserProfile,
    isGuest: Boolean,
    onBack: () -> Unit,
    onGameClick: () -> Unit,
    onViewStats: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFriend by rememberSaveable(user.name) { mutableStateOf(user.isFriend) }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    val profile = remember(user) { UserProfileMockData.profile(user) }

    val guestAddFriend = stringResource(R.string.user_profile_guest_add_friend)
    val guestMessage = stringResource(R.string.user_profile_guest_message)

    val onFriendAction = {
        if (isGuest) toastMessage = guestAddFriend else isFriend = !isFriend
    }
    val onMessage = {
        if (isGuest) toastMessage = guestMessage else Unit
    }

    Box(modifier = modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(username = user.username, onBack = onBack)

            LazyColumn(contentPadding = PaddingValues(bottom = 32.dp)) {
                item {
                    val context = LocalContext.current
                    ProfileHeader(
                        profile = profile,
                        mode = ProfileHeaderMode.Other(isFriend = isFriend),
                        isGuest = isGuest,
                        onShare = { context.shareText("Check out ${user.name}'s gaming profile on GameTrackr!") },
                        onAddFriend = onFriendAction,
                        onMessage = onMessage,
                    )
                    Spacer(Modifier.height(26.dp))
                }

                item {
                    ProfileStatsBar(
                        stats = profile.stats,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        onClick = onViewStats,
                    )
                    Spacer(Modifier.height(26.dp))
                }

                item {
                    SectionTitle(stringResource(R.string.profile_section_breakdown))
                    Spacer(Modifier.height(14.dp))
                    ProfileBreakdownSection(
                        breakdown = UserProfileMockData.breakdown,
                        onSelect = {},
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(Modifier.height(26.dp))
                }

                item {
                    SectionTitle(stringResource(R.string.profile_section_favorites))
                    Spacer(Modifier.height(14.dp))
                    ProfileFavoritesRow(favorites = UserProfileMockData.favorites, onSelect = { onGameClick() })
                    Spacer(Modifier.height(26.dp))
                }

                item {
                    SectionTitle(stringResource(R.string.profile_section_currently_playing))
                    Spacer(Modifier.height(14.dp))
                }

                items(UserProfileMockData.currentlyPlaying, key = { it.title }) { entry ->
                    val interactionSource = remember { MutableInteractionSource() }
                    LibraryEntryRow(
                        entry = entry,
                        modifier =
                            Modifier
                                .pressScale(interactionSource)
                                .padding(horizontal = 20.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClickLabel = entry.title,
                                    role = Role.Button,
                                    onClick = onGameClick,
                                ),
                    )
                    Spacer(Modifier.height(14.dp))
                }

                item {
                    Spacer(Modifier.height(12.dp))
                    SectionTitle(stringResource(R.string.profile_section_activity))
                    Spacer(Modifier.height(14.dp))
                    ProfileActivitySection(
                        events = UserProfileMockData.activity,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }
        }

        Toast(
            message = toastMessage,
            onDismiss = { toastMessage = null },
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun TopBar(
    username: String,
    onBack: () -> Unit,
) {
    val backInteraction = remember { MutableInteractionSource() }
    val backLabel = stringResource(R.string.user_profile_back)

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .pressScale(backInteraction)
                    .size(40.dp)
                    .clickable(
                        interactionSource = backInteraction,
                        indication = null,
                        onClickLabel = backLabel,
                        role = Role.Button,
                        onClick = onBack,
                    ),
            contentAlignment = Alignment.CenterStart,
        ) {
            Icon(
                imageVector = AppIcon.BACK.image(),
                contentDescription = backLabel,
                tint = AppTextPrimary,
                modifier = Modifier.size(22.dp),
            )
        }

        Text(
            text = username,
            color = AppTextPrimary,
            style = AppType.headline(18.sp),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = AppTextPrimary,
        style = AppType.headline(20.sp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
    )
}
