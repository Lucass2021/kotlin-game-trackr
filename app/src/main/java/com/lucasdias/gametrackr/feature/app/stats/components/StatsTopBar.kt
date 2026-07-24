package com.lucasdias.gametrackr.feature.app.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.BackButton
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType

@Composable
fun StatsTopBar(
    title: String,
    onBack: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shareLabel = stringResource(R.string.stats_share)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(AppBackground)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BackButton(onBack = onBack)

        Text(
            text = title,
            color = AppTextPrimary,
            style = AppType.headline(22.sp),
            modifier = Modifier.weight(1f),
        )

        Icon(
            imageVector = AppIcon.SHARE.image(),
            contentDescription = shareLabel,
            tint = AppSecondary,
            modifier =
                Modifier
                    .pressScale(interactionSource)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClickLabel = shareLabel,
                        role = Role.Button,
                        onClick = onShare,
                    ).size(22.dp),
        )
    }
}
