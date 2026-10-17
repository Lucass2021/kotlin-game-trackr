package com.lucasdias.gametrackr.feature.app.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.model.Profile
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileContent
import com.lucasdias.gametrackr.feature.app.profile.components.ProfileGuestState

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    isGuest: Boolean,
    profile: Profile,
    setups: List<SetupItem>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onGameClick: (String) -> Unit = {},
    onStatusClick: (LibraryStatus?) -> Unit = {},
    onEditProfile: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    onViewStats: () -> Unit = {},
    onViewSetup: () -> Unit = {},
) {
    val overview = state.overview
    when {
        isGuest -> {
            ProfileGuestState(onCreateAccount = onCreateAccount, modifier = modifier)
        }

        overview != null -> {
            ProfileContent(
                overview = overview,
                profile = profile,
                setups = setups,
                onGameClick = onGameClick,
                onStatusClick = onStatusClick,
                onEditProfile = onEditProfile,
                onViewStats = onViewStats,
                onViewSetup = onViewSetup,
                modifier = modifier,
            )
        }

        state.hasError -> {
            EmptyState(
                icon = AppIcon.INFO,
                title = stringResource(R.string.profile_load_error_title),
                message = stringResource(R.string.profile_load_error_message),
                actionTitle = stringResource(R.string.profile_retry),
                onAction = onRetry,
                modifier = modifier.fillMaxSize().background(AppBackground),
            )
        }

        else -> {
            Box(modifier = modifier.fillMaxSize().background(AppBackground), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppPrimary)
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenLoadingPreview() {
    AppPreview {
        ProfileScreen(
            state = ProfileUiState(isLoading = true),
            isGuest = false,
            profile = ProfileMockData.profile,
            setups = emptyList(),
            onRetry = {},
        )
    }
}

@Preview
@Composable
private fun ProfileScreenErrorPreview() {
    AppPreview {
        ProfileScreen(
            state = ProfileUiState(hasError = true),
            isGuest = false,
            profile = ProfileMockData.profile,
            setups = emptyList(),
            onRetry = {},
        )
    }
}
