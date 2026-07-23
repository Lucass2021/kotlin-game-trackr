package com.lucasdias.gametrackr.feature.app.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.library.LibraryStatus
import com.lucasdias.gametrackr.feature.app.library.components.LibraryEntryRow
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileActivitySection
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileBreakdownSection
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileFavoritesRow
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileHeader
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileStatsBar

@Composable
fun ProfileScreen(
    isGuest: Boolean,
    onGameClick: () -> Unit,
    onStatusClick: (LibraryStatus) -> Unit,
    onEditProfile: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
    profile: Profile = ProfileMockData.profile,
) {
    if (isGuest) {
        GuestState(onCreateAccount = onCreateAccount, modifier = modifier)
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(AppBackground),
        contentPadding = PaddingValues(bottom = 32.dp),
    ) {
        item {
            ProfileHeader(profile = profile, onEdit = onEditProfile, onShare = {})
            Spacer(Modifier.height(26.dp))
        }

        item {
            ProfileStatsBar(stats = profile.stats, modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(Modifier.height(26.dp))
        }

        item {
            SectionHeader(title = stringResource(R.string.profile_section_breakdown))
            Spacer(Modifier.height(14.dp))
            ProfileBreakdownSection(
                breakdown = ProfileMockData.breakdown,
                onSelect = onStatusClick,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            Spacer(Modifier.height(26.dp))
        }

        item {
            SectionHeader(title = stringResource(R.string.profile_section_favorites))
            Spacer(Modifier.height(14.dp))
            ProfileFavoritesRow(favorites = ProfileMockData.favorites, onSelect = { onGameClick() })
            Spacer(Modifier.height(26.dp))
        }

        item {
            SectionHeader(
                title = stringResource(R.string.profile_section_currently_playing),
                actionTitle = stringResource(R.string.profile_section_view_all),
                onAction = { onStatusClick(LibraryStatus.PLAYING) },
            )
            Spacer(Modifier.height(14.dp))
        }

        items(ProfileMockData.currentlyPlaying, key = { it.title }) { entry ->
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
            SectionHeader(title = stringResource(R.string.profile_section_activity))
            Spacer(Modifier.height(14.dp))
            ProfileActivitySection(
                events = ProfileMockData.activity,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionTitle: String? = null,
    onAction: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = AppTextPrimary,
            style = AppType.headline(20.sp),
            modifier = Modifier.weight(1f),
        )

        if (actionTitle != null) {
            Text(
                text = actionTitle,
                color = AppPrimary,
                style = AppType.label(15.sp),
                modifier =
                    Modifier
                        .pressScale(interactionSource)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            role = Role.Button,
                            onClick = onAction,
                        ),
            )
        }
    }
}

@Composable
private fun GuestState(
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().background(AppBackground).padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = AppIcon.AVATAR.image(),
            contentDescription = null,
            tint = AppPrimary,
            modifier = Modifier.size(40.dp),
        )

        Text(
            text = stringResource(R.string.profile_guest_title),
            color = AppTextPrimary,
            style = AppType.headline(22.sp),
        )

        Text(
            text = stringResource(R.string.profile_guest_subtitle),
            color = AppTextSecondary,
            style = AppType.body(15.sp),
            textAlign = TextAlign.Center,
        )

        PrimaryButton(
            text = stringResource(R.string.profile_guest_action),
            onClick = onCreateAccount,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}
