package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.ProfileOverview
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.data.profile.UserProfileMockData
import com.lucasdias.gametrackr.feature.app.library.components.LibraryEntryRow
import com.lucasdias.gametrackr.feature.app.profile.ProfileHeaderMode

@Composable
fun UserProfileContent(
    profile: Profile,
    overview: ProfileOverview,
    isFriend: Boolean,
    isGuest: Boolean,
    onShare: () -> Unit,
    onFriendAction: () -> Unit,
    onMessage: () -> Unit,
    onGameClick: (String) -> Unit,
    onViewStats: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(bottom = Spacing.xxl)) {
        item {
            ProfileHeader(
                profile = profile,
                mode = ProfileHeaderMode.Other(isFriend = isFriend),
                isGuest = isGuest,
                onShare = onShare,
                onAddFriend = onFriendAction,
                onMessage = onMessage,
            )
            Spacer(Modifier.height(Spacing.xl))
            ProfileStatsBar(
                stats = overview.stats,
                modifier = Modifier.padding(horizontal = Spacing.lg),
                onClick = onViewStats,
            )
            Spacer(Modifier.height(Spacing.xl))
        }

        item {
            ProfileSectionHeader(title = stringResource(R.string.profile_section_breakdown))
            Spacer(Modifier.height(Spacing.sm))
            ProfileBreakdownSection(
                breakdown = overview.breakdown,
                onSelect = {},
                modifier = Modifier.padding(horizontal = Spacing.lg),
            )
            Spacer(Modifier.height(Spacing.xl))
            ProfileSectionHeader(title = stringResource(R.string.profile_section_favorites))
            Spacer(Modifier.height(Spacing.sm))
            ProfileFavoritesSection(favorites = overview.favorites, onSelect = { onGameClick(it.slug) })
            Spacer(Modifier.height(Spacing.xl))
            ProfileSectionHeader(title = stringResource(R.string.profile_section_currently_playing))
            Spacer(Modifier.height(Spacing.sm))
        }

        items(overview.currentlyPlaying, key = { it.title }) { entry ->
            val interactionSource = remember { MutableInteractionSource() }
            LibraryEntryRow(
                entry = entry,
                modifier =
                    Modifier
                        .pressScale(interactionSource)
                        .padding(horizontal = Spacing.lg)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClickLabel = entry.title,
                            role = Role.Button,
                            onClick = { onGameClick(entry.slug) },
                        ),
            )
            Spacer(Modifier.height(Spacing.sm))
        }

        item {
            Spacer(Modifier.height(Spacing.sm))
            ProfileSectionHeader(title = stringResource(R.string.profile_section_activity))
            Spacer(Modifier.height(Spacing.sm))
            ProfileActivitySection(
                events = overview.activity,
                modifier = Modifier.padding(horizontal = Spacing.lg),
            )
        }
    }
}

@Preview
@Composable
private fun UserProfileContentPreview() {
    AppPreview {
        UserProfileContent(
            profile = ProfileMockData.profile,
            overview = UserProfileMockData.overview,
            isFriend = false,
            isGuest = false,
            onShare = {},
            onFriendAction = {},
            onMessage = {},
            onGameClick = {},
            onViewStats = {},
        )
    }
}
