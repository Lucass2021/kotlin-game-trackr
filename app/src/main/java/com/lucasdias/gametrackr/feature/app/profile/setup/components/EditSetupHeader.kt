package com.lucasdias.gametrackr.feature.app.profile.setup.components

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

@Composable
fun EditSetupHeader(
    isNew: Boolean,
    isSaving: Boolean,
    onClose: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val closeInteraction = remember { MutableInteractionSource() }
    val saveInteraction = remember { MutableInteractionSource() }
    val closeLabel = stringResource(R.string.setup_close)

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .pressScale(closeInteraction)
                    .size(40.dp)
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
                imageVector = AppIcon.CLOSE.image(),
                contentDescription = closeLabel,
                tint = AppTextPrimary,
                modifier = Modifier.size(20.dp),
            )
        }

        Text(
            text = stringResource(if (isNew) R.string.setup_new_title else R.string.setup_edit_title),
            color = AppTextPrimary,
            style = AppType.headline(20.sp),
            modifier = Modifier.weight(1f),
        )

        Box(
            modifier =
                Modifier
                    .pressScale(saveInteraction)
                    .height(38.dp)
                    .clip(CircleShape)
                    .background(AppPrimary)
                    .clickable(
                        interactionSource = saveInteraction,
                        indication = null,
                        role = Role.Button,
                        onClick = onSave,
                    ).padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (isSaving) {
                CircularProgressIndicator(color = AppOnPrimary, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
            } else {
                Text(text = stringResource(R.string.setup_save), color = AppOnPrimary, style = AppType.label(15.sp))
            }
        }
    }
}

@Preview
@Composable
private fun EditSetupHeaderPreview() {
    AppPreview {
        EditSetupHeader(isNew = true, isSaving = false, onClose = {}, onSave = {})
    }
}
