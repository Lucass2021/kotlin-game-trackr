package com.lucasdias.gametrackr.feature.app.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.profile.SetupItem

@Composable
fun ProfileSetupRow(
    setups: List<SetupItem>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(setups, key = { it.title }) { setup ->
            SetupCard(setup = setup)
        }
    }
}

@Composable
private fun SetupCard(setup: SetupItem) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .width(160.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = setup.title,
                    role = Role.Button,
                    onClick = {},
                ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .width(160.dp)
                    .height(120.dp)
                    .clip(shape)
                    .background(Brush.linearGradient(listOf(setup.imageStart, setup.imageEnd))),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.BRAND.image(),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.15f),
                modifier = Modifier.height(40.dp),
            )
        }

        Text(
            text = setup.title,
            color = AppTextPrimary,
            style = AppType.label(14.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = setup.description,
            color = AppTextSecondary,
            style = AppType.body(12.sp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
