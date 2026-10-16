package com.lucasdias.gametrackr.feature.app.profile.setup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
fun MySetupHeader(
    showAdd: Boolean,
    onBack: () -> Unit,
    onAdd: () -> Unit,
) {
    val backInteraction = remember { MutableInteractionSource() }
    val addInteraction = remember { MutableInteractionSource() }
    val backLabel = stringResource(R.string.setup_back)
    val addLabel = stringResource(R.string.setup_add)

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .pressScale(backInteraction)
                    .size(40.dp)
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
                modifier = Modifier.size(22.dp),
            )
        }

        Text(
            text = stringResource(R.string.setup_title),
            color = AppTextPrimary,
            style = AppType.headline(20.sp),
            modifier = Modifier.weight(1f),
        )

        if (showAdd) {
            Box(
                modifier =
                    Modifier
                        .pressScale(addInteraction)
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(AppPrimary)
                        .clickable(
                            interactionSource = addInteraction,
                            indication = null,
                            onClickLabel = addLabel,
                            role = Role.Button,
                            onClick = onAdd,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcon.PLUS.image(),
                    contentDescription = addLabel,
                    tint = AppOnPrimary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun MySetupHeaderPreview() {
    AppPreview {
        MySetupHeader(showAdd = true, onBack = {}, onAdd = {})
    }
}
