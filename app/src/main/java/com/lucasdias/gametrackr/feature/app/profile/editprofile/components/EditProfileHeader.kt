package com.lucasdias.gametrackr.feature.app.profile.editprofile.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val BACK_SIZE = 40.dp
private val BACK_ICON_SIZE = 22.dp
private val TITLE_SIZE = 20.sp
private val SAVE_HEIGHT = 38.dp
private val SAVE_TEXT_SIZE = 15.sp
private val SPINNER_SIZE = 18.dp
private val SPINNER_STROKE = 2.dp

@Composable
fun EditProfileHeader(
    isSaving: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backInteraction = remember { MutableInteractionSource() }
    val saveInteraction = remember { MutableInteractionSource() }
    val backLabel = stringResource(R.string.edit_profile_back)
    val savingLabel = stringResource(R.string.edit_profile_saving)

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.md, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Box(
            modifier =
                Modifier
                    .pressScale(backInteraction)
                    .size(BACK_SIZE)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = backInteraction,
                        indication = null,
                        onClickLabel = backLabel,
                        role = Role.Button,
                        onClick = onBack,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.BACK.image(),
                contentDescription = backLabel,
                tint = AppTextPrimary,
                modifier = Modifier.size(BACK_ICON_SIZE),
            )
        }

        Text(
            text = stringResource(R.string.edit_profile_title),
            color = AppTextPrimary,
            style = AppType.headline(TITLE_SIZE),
            modifier = Modifier.weight(1f),
        )

        Box(
            modifier =
                Modifier
                    .pressScale(saveInteraction)
                    .height(SAVE_HEIGHT)
                    .clip(CircleShape)
                    .background(AppPrimary)
                    .clickable(
                        interactionSource = saveInteraction,
                        indication = null,
                        role = Role.Button,
                        onClick = onSave,
                    ).padding(horizontal = Spacing.lg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.edit_profile_save),
                color = if (isSaving) AppPrimary else AppOnPrimary,
                style = AppType.label(SAVE_TEXT_SIZE),
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
private fun EditProfileHeaderPreview() {
    AppPreview {
        EditProfileHeader(isSaving = false, onBack = {}, onSave = {})
    }
}

@Preview
@Composable
private fun EditProfileHeaderSavingPreview() {
    AppPreview {
        EditProfileHeader(isSaving = true, onBack = {}, onSave = {})
    }
}
