package com.lucasdias.gametrackr.feature.app.profile.setup.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppType

@Composable
fun SetupDeleteButton(
    isDeleting: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(14.dp)
    val label = stringResource(R.string.setup_delete)

    Row(
        modifier =
            modifier
                .pressScale(interactionSource)
                .fillMaxWidth()
                .height(52.dp)
                .clip(shape)
                .border(1.dp, AppTertiary.copy(alpha = 0.4f), shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onClick,
                ),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isDeleting) {
            CircularProgressIndicator(color = AppTertiary, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
        } else {
            Icon(
                imageVector = AppIcon.TRASH.image(),
                contentDescription = null,
                tint = AppTertiary,
                modifier = Modifier.size(18.dp),
            )
        }
        Text(text = label, color = AppTertiary, style = AppType.label(16.sp))
    }
}

@Preview
@Composable
private fun SetupDeleteButtonPreview() {
    AppPreview {
        SetupDeleteButton(isDeleting = false, onClick = {}, modifier = Modifier.padding(20.dp))
    }
}
