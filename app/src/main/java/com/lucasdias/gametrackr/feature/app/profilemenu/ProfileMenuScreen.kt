package com.lucasdias.gametrackr.feature.app.profilemenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.components.SecondaryButton
import com.lucasdias.gametrackr.core.ui.text.asString
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.data.profile.ProfileMockData
import com.lucasdias.gametrackr.feature.app.appshell.components.DetailTopBar
import com.lucasdias.gametrackr.feature.app.profilemenu.components.ProfileMenuAccountHeader
import com.lucasdias.gametrackr.feature.app.profilemenu.components.ProfileMenuSection

@Composable
fun ProfileMenuScreen(
    state: ProfileMenuUiState,
    actions: ProfileMenuActions,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(AppBackground)
                .statusBarsPadding()
                .navigationBarsPadding(),
    ) {
        DetailTopBar(title = stringResource(R.string.menu_title), onBack = actions.onBack)
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.lg, vertical = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.xl),
        ) {
            ProfileMenuAccountHeader(name = state.name.asString(), subtitle = state.subtitle.asString())
            if (state.accountItems.isNotEmpty()) {
                ProfileMenuSection(items = state.accountItems, onItemSelect = actions.onItemSelect)
            }
            ProfileMenuSection(items = state.appItems, onItemSelect = actions.onItemSelect)
        }

        val buttonModifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.lg, vertical = Spacing.sm)
        if (state.isGuest) {
            PrimaryButton(
                text = stringResource(R.string.menu_create_account),
                onClick = actions.onLogout,
                modifier = buttonModifier,
            )
        } else {
            SecondaryButton(text = stringResource(R.string.sign_out), onClick = actions.onLogout, modifier = buttonModifier)
        }
    }
}

@Preview
@Composable
private fun ProfileMenuScreenPreview() {
    AppPreview {
        ProfileMenuScreen(
            state =
                ProfileMenuUiState.from(
                    isGuest = false,
                    userName = ProfileMockData.user.name,
                    email = ProfileMockData.user.email,
                ),
            actions = ProfileMenuActions(),
        )
    }
}

@Preview
@Composable
private fun ProfileMenuScreenGuestPreview() {
    AppPreview {
        ProfileMenuScreen(
            state = ProfileMenuUiState.from(isGuest = true, userName = null, email = null),
            actions = ProfileMenuActions(),
        )
    }
}
