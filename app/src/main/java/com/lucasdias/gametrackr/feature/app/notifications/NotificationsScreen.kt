package com.lucasdias.gametrackr.feature.app.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.NotificationFilter
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.notifications.NotificationsMockData
import com.lucasdias.gametrackr.feature.app.notifications.components.NotificationFilterTabs
import com.lucasdias.gametrackr.feature.app.notifications.components.NotificationsList
import com.lucasdias.gametrackr.feature.app.notifications.components.NotificationsTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotificationsScreen(
    isGuest: Boolean,
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    viewModel: NotificationsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(isGuest) {
        if (!isGuest) viewModel.loadIfNeeded()
    }

    NotificationsScreen(
        state = state,
        isGuest = isGuest,
        onBack = onBack,
        onCreateAccount = onCreateAccount,
        onRetry = viewModel::load,
        onLoadMore = viewModel::loadMore,
        onFilterChange = viewModel::onFilterChange,
        onMarkAllRead = viewModel::onMarkAllRead,
        onClearAll = viewModel::onClearAll,
        onAcceptFriendRequest = viewModel::onAcceptFriendRequest,
        onDeclineFriendRequest = viewModel::onDeclineFriendRequest,
        onErrorShown = viewModel::onErrorShown,
    )
}

@Composable
private fun NotificationsScreen(
    state: NotificationsUiState,
    isGuest: Boolean,
    onBack: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    onRetry: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    onFilterChange: (NotificationFilter) -> Unit = {},
    onMarkAllRead: () -> Unit = {},
    onClearAll: () -> Unit = {},
    onAcceptFriendRequest: (Int) -> Unit = {},
    onDeclineFriendRequest: (Int) -> Unit = {},
    onErrorShown: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        Column(modifier = Modifier.fillMaxSize()) {
            NotificationsTopBar(
                onBack = onBack,
                onMarkAllRead = onMarkAllRead,
                onClearAll = onClearAll,
                showActions = !isGuest,
            )

            if (isGuest) {
                EmptyState(
                    icon = AppIcon.NOTIFICATIONS,
                    title = stringResource(R.string.notifications_guest_title),
                    message = stringResource(R.string.notifications_guest_subtitle),
                    actionTitle = stringResource(R.string.menu_create_account),
                    onAction = onCreateAccount,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                NotificationFilterTabs(
                    selected = state.filter,
                    onSelect = onFilterChange,
                    modifier = Modifier.padding(top = Spacing.xxs, bottom = Spacing.xs),
                )

                when {
                    state.hasError -> {
                        EmptyState(
                            icon = AppIcon.INFO,
                            title = stringResource(R.string.notifications_load_error_title),
                            message = stringResource(R.string.notifications_load_error_message),
                            actionTitle = stringResource(R.string.notifications_retry),
                            onAction = onRetry,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    state.currentPage == 0 -> {
                        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = AppPrimary)
                        }
                    }

                    state.items.isEmpty() -> {
                        EmptyState(
                            icon = AppIcon.NOTIFICATIONS,
                            title = stringResource(R.string.notifications_empty_title),
                            message = stringResource(R.string.notifications_empty_subtitle),
                            modifier = Modifier.weight(1f),
                        )
                    }

                    else -> {
                        NotificationsList(
                            sections = state.sections,
                            canLoadMore = state.canLoadMore,
                            isLoadingMore = state.isLoadingMore,
                            loadMoreFailed = state.loadMoreFailed,
                            onLoadMore = onLoadMore,
                            onAcceptFriendRequest = onAcceptFriendRequest,
                            onDeclineFriendRequest = onDeclineFriendRequest,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }

        Toast(
            message = state.errorMessage?.let { stringResource(it) },
            onDismiss = onErrorShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Preview
@Composable
private fun NotificationsScreenPreview() {
    AppPreview {
        NotificationsScreen(
            state = NotificationsUiState(items = NotificationsMockData.items, currentPage = 1),
            isGuest = false,
        )
    }
}

@Preview
@Composable
private fun NotificationsScreenErrorPreview() {
    AppPreview {
        NotificationsScreen(
            state = NotificationsUiState(hasError = true),
            isGuest = false,
        )
    }
}

@Preview
@Composable
private fun NotificationsScreenGuestPreview() {
    AppPreview {
        NotificationsScreen(state = NotificationsUiState(), isGuest = true)
    }
}
