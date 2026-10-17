package com.lucasdias.gametrackr.feature.app.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.UserProfile
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.shareText
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.data.profile.UserProfileMockData
import com.lucasdias.gametrackr.feature.app.profile.components.UserProfileContent
import com.lucasdias.gametrackr.feature.app.profile.components.UserProfileTopBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UserProfileScreen(
    user: UserProfile,
    isGuest: Boolean,
    onBack: () -> Unit,
    onGameClick: (String) -> Unit,
    onViewStats: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = koinViewModel { parametersOf(user) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) { viewModel.loadIfNeeded() }

    UserProfileScreen(
        state = state,
        user = user,
        isGuest = isGuest,
        onBack = onBack,
        onRetry = viewModel::load,
        onFriendAction = { viewModel.onFriendAction(isGuest) },
        onMessage = { viewModel.onMessage(isGuest) },
        onToastShown = viewModel::onToastShown,
        onGameClick = onGameClick,
        onViewStats = onViewStats,
        modifier = modifier,
    )
}

@Composable
private fun UserProfileScreen(
    state: UserProfileUiState,
    user: UserProfile,
    isGuest: Boolean,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onRetry: () -> Unit = {},
    onFriendAction: () -> Unit = {},
    onMessage: () -> Unit = {},
    onToastShown: () -> Unit = {},
    onGameClick: (String) -> Unit = {},
    onViewStats: () -> Unit = {},
) {
    val context = LocalContext.current
    val shareMessage = stringResource(R.string.user_profile_share_message, user.name)

    Box(modifier = modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        Column(modifier = Modifier.fillMaxSize()) {
            UserProfileTopBar(username = user.username, onBack = onBack)

            val profile = state.profile
            val overview = state.overview
            when {
                profile != null && overview != null -> {
                    UserProfileContent(
                        profile = profile,
                        overview = overview,
                        isFriend = state.isFriend,
                        isGuest = isGuest,
                        onShare = { context.shareText(shareMessage) },
                        onFriendAction = onFriendAction,
                        onMessage = onMessage,
                        onGameClick = onGameClick,
                        onViewStats = onViewStats,
                        modifier = Modifier.weight(1f),
                    )
                }

                state.hasError -> {
                    EmptyState(
                        icon = AppIcon.INFO,
                        title = stringResource(R.string.user_profile_load_error_title),
                        message = stringResource(R.string.user_profile_load_error_message),
                        actionTitle = stringResource(R.string.user_profile_retry),
                        onAction = onRetry,
                        modifier = Modifier.weight(1f),
                    )
                }

                else -> {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AppPrimary)
                    }
                }
            }
        }

        Toast(
            message = state.toastMessage?.let { stringResource(it) },
            onDismiss = onToastShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Preview
@Composable
private fun UserProfileScreenPreview() {
    AppPreview {
        UserProfileScreen(
            state =
                UserProfileUiState(
                    profile = ProfileMockData.profile,
                    overview = UserProfileMockData.overview,
                    isFriend = true,
                ),
            user = UserProfileMockData.friend,
            isGuest = false,
        )
    }
}

@Preview
@Composable
private fun UserProfileScreenErrorPreview() {
    AppPreview {
        UserProfileScreen(state = UserProfileUiState(hasError = true), user = UserProfileMockData.user, isGuest = false)
    }
}
