package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.ProfileOverview
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.shareText
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.library.components.LibraryEntryRow

@Composable
fun ProfileContent(
    overview: ProfileOverview,
    profile: Profile,
    setups: List<SetupItem>,
    onGameClick: (String) -> Unit,
    onStatusClick: (LibraryStatus?) -> Unit,
    onEditProfile: () -> Unit,
    onViewStats: () -> Unit,
    onViewSetup: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val shareMessage = stringResource(R.string.profile_share_message)
    val viewAll = stringResource(R.string.profile_section_view_all)

    LazyColumn(
        modifier = modifier.fillMaxSize().background(AppBackground),
        contentPadding = PaddingValues(bottom = Spacing.xxl),
    ) {
        item {
            ProfileHeader(profile = profile, onEdit = onEditProfile, onShare = { context.shareText(shareMessage) })
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
                onSelect = onStatusClick,
                modifier = Modifier.padding(horizontal = Spacing.lg),
            )
            Spacer(Modifier.height(Spacing.xl))
        }

        item {
            ProfileSectionHeader(
                title = stringResource(R.string.profile_section_favorites),
                actionTitle = viewAll,
                onAction = { onStatusClick(null) },
            )
            Spacer(Modifier.height(Spacing.sm))
            ProfileFavoritesSection(favorites = overview.favorites, onSelect = { onGameClick(it.slug) })
            Spacer(Modifier.height(Spacing.xl))
            ProfileSectionHeader(
                title = stringResource(R.string.profile_section_currently_playing),
                actionTitle = viewAll,
                onAction = { onStatusClick(LibraryStatus.PLAYING) },
            )
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
            ProfileSectionHeader(
                title = stringResource(R.string.profile_section_setup),
                actionTitle = viewAll.takeIf { setups.isNotEmpty() },
                onAction = onViewSetup,
            )
            Spacer(Modifier.height(Spacing.sm))
            ProfileSetupSection(setups = setups, onSelect = { onViewSetup() }, onAdd = onViewSetup)
            Spacer(Modifier.height(Spacing.xl))
        }
    }
}

@Preview
@Composable
private fun ProfileContentPreview() {
    AppPreview {
        ProfileContent(
            overview = ProfileMockData.overview,
            profile = ProfileMockData.profile,
            setups = ProfileMockData.setups,
            onGameClick = {},
            onStatusClick = {},
            onEditProfile = {},
            onViewStats = {},
            onViewSetup = {},
        )
    }
}
