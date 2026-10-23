package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val CLOSE_SIZE = 40.dp
private val CLOSE_ICON_SIZE = 22.dp
private val TITLE_SIZE = 20.sp
private val ACTION_HEIGHT = 38.dp
private val ACTION_TEXT_SIZE = 15.sp
private val SPINNER_SIZE = 18.dp
private val SPINNER_STROKE = 2.dp

@Composable
fun FormHeader(
    title: String,
    actionLabel: String,
    closeLabel: String,
    savingLabel: String,
    isSaving: Boolean,
    onClose: () -> Unit,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    closeIcon: AppIcon = AppIcon.BACK,
) {
    val closeInteraction = remember { MutableInteractionSource() }
    val actionInteraction = remember { MutableInteractionSource() }

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.md, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Box(
            modifier =
                Modifier
                    .pressScale(closeInteraction)
                    .size(CLOSE_SIZE)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = closeInteraction,
                        indication = null,
                        onClickLabel = closeLabel,
                        role = Role.Button,
                        onClick = onClose,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = closeIcon.image(),
                contentDescription = closeLabel,
                tint = AppTextPrimary,
                modifier = Modifier.size(CLOSE_ICON_SIZE),
            )
        }

        Text(
            text = title,
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE),
            modifier = Modifier.weight(1f),
        )

        Box(
            modifier =
                Modifier
                    .pressScale(actionInteraction)
                    .height(ACTION_HEIGHT)
                    .clip(CircleShape)
                    .background(AppPrimary)
                    .clickable(
                        interactionSource = actionInteraction,
                        indication = null,
                        role = Role.Button,
                        onClick = onAction,
                    ).padding(horizontal = Spacing.lg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = actionLabel,
                color = if (isSaving) AppPrimary else AppOnPrimary,
                style = AppType.label(ACTION_TEXT_SIZE),
            )
            if (isSaving) {
                CircularProgressIndicator(
                    color = AppOnPrimary,
                    strokeWidth = SPINNER_STROKE,
                    modifier = Modifier.size(SPINNER_SIZE).semantics { contentDescription = savingLabel },
                )
            }
        }
    }
}

@Preview
@Composable
private fun FormHeaderPreview() {
    AppPreview {
        FormHeader(
            title = "Edit profile",
            actionLabel = "Save",
            closeLabel = "Back",
            savingLabel = "Saving",
            isSaving = false,
            onClose = {},
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun FormHeaderSavingPreview() {
    AppPreview {
        FormHeader(
            title = "New community",
            actionLabel = "Create",
            closeLabel = "Close",
            savingLabel = "Creating",
            isSaving = true,
            onClose = {},
            onAction = {},
            closeIcon = AppIcon.CLOSE,
        )
    }
}
