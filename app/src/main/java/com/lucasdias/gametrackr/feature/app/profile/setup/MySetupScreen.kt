package com.lucasdias.gametrackr.feature.app.profile.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.profile.setup.components.MySetupHeader
import com.lucasdias.gametrackr.feature.app.profile.setup.components.SetupCard

@Composable
fun MySetupScreen(
    state: MySetupUiState,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (SetupItem) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        MySetupHeader(showAdd = state.setups.isNotEmpty(), onBack = onBack, onAdd = onAdd)

        when {
            state.setups.isNotEmpty() -> {
                SetupList(setups = state.setups, onEdit = onEdit)
            }

            state.isLoading -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppPrimary)
                }
            }

            state.hasError -> {
                EmptyState(
                    icon = AppIcon.INFO,
                    title = stringResource(R.string.setup_load_error_title),
                    message = stringResource(R.string.setup_load_error_message),
                    actionTitle = stringResource(R.string.setup_retry),
                    onAction = onRetry,
                    modifier = Modifier.weight(1f),
                )
            }

            else -> {
                EmptyState(
                    icon = AppIcon.DEVICES,
                    title = stringResource(R.string.setup_empty_title),
                    message = stringResource(R.string.setup_empty_message),
                    actionTitle = stringResource(R.string.setup_empty_action),
                    onAction = onAdd,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun SetupList(
    setups: List<SetupItem>,
    onEdit: (SetupItem) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        items(setups, key = { it.id }) { setup ->
            SetupCard(setup = setup, onClick = { onEdit(setup) })
        }
    }
}

@Preview
@Composable
private fun MySetupScreenPreview() {
    AppPreview {
        MySetupScreen(
            state = MySetupUiState(setups = ProfileMockData.setups),
            onBack = {},
            onAdd = {},
            onEdit = {},
            onRetry = {},
        )
    }
}

@Preview
@Composable
private fun MySetupScreenEmptyPreview() {
    AppPreview {
        MySetupScreen(state = MySetupUiState(), onBack = {}, onAdd = {}, onEdit = {}, onRetry = {})
    }
}
