package com.lucasdias.gametrackr.feature.app.profile.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.withResumed
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary

sealed interface EditSetupTarget {
    data object New : EditSetupTarget

    data class Existing(
        val setup: SetupItem,
    ) : EditSetupTarget

    data object Loading : EditSetupTarget

    data object Missing : EditSetupTarget
}

fun editSetupTarget(
    setupId: Int?,
    resolved: SetupItem?,
    mySetupState: MySetupUiState,
): EditSetupTarget {
    if (setupId == null) return EditSetupTarget.New
    val setup = resolved ?: mySetupState.setups.firstOrNull { it.id == setupId }
    return when {
        setup != null -> EditSetupTarget.Existing(setup)
        mySetupState.isLoading -> EditSetupTarget.Loading
        else -> EditSetupTarget.Missing
    }
}

@Composable
fun EditSetupRoute(
    setupId: Int?,
    mySetupState: MySetupUiState,
    onBack: () -> Unit,
    onSaved: (SetupItem) -> Unit,
    onDeleted: (Int) -> Unit,
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var resolved by remember { mutableStateOf<SetupItem?>(null) }
    val target = editSetupTarget(setupId, resolved, mySetupState)

    LaunchedEffect(target) {
        if (target is EditSetupTarget.Existing) resolved = target.setup
    }

    when (target) {
        EditSetupTarget.New -> {
            EditSetupScreen(setup = null, onBack = onBack, onSaved = onSaved, onDeleted = onDeleted)
        }

        is EditSetupTarget.Existing -> {
            EditSetupScreen(setup = target.setup, onBack = onBack, onSaved = onSaved, onDeleted = onDeleted)
        }

        EditSetupTarget.Loading -> {
            Box(modifier = Modifier.fillMaxSize().background(AppBackground), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppPrimary)
            }
        }

        EditSetupTarget.Missing -> {
            LaunchedEffect(Unit) { lifecycle.withResumed { onBack() } }
        }
    }
}

@Preview
@Composable
private fun EditSetupRouteLoadingPreview() {
    AppPreview {
        EditSetupRoute(
            setupId = 1,
            mySetupState = MySetupUiState(isLoading = true),
            onBack = {},
            onSaved = {},
            onDeleted = {},
        )
    }
}
