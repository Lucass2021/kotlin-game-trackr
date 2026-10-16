package com.lucasdias.gametrackr.feature.app.profile.setup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.data.profile.ProfileMockData

@Composable
fun SetupCard(
    setup: SetupItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(20.dp)

    Column(
        modifier =
            modifier
                .pressScale(interactionSource)
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = setup.title,
                    role = Role.Button,
                    onClick = onClick,
                ).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SetupCover(
            setup = setup,
            cornerRadius = 16,
            placeholderSize = 48,
            modifier = Modifier.fillMaxWidth().height(180.dp),
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = setup.title,
                color = AppTextPrimary,
                style = AppType.headline(18.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (setup.description.isNotEmpty()) {
                Text(
                    text = setup.description,
                    color = AppTextSecondary,
                    style = AppType.body(14.sp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SetupCardPreview() {
    AppPreview {
        SetupCard(setup = ProfileMockData.setups[0], onClick = {}, modifier = Modifier.padding(20.dp))
    }
}
