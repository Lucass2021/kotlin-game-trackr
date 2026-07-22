package com.lucasdias.gametrackr.feature.app.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.subtleBounce
import com.lucasdias.gametrackr.core.ui.components.glow
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType

@Composable
fun LibraryEmptyState(
    onBrowse: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .subtleBounce()
                    .glow(color = AppPrimary, cornerRadius = 32.dp, blurRadius = 34.dp, alpha = 0.25f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(AppSurfaceCard)
                    .size(128.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.BRAND.image(),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(60.dp),
            )
        }

        Text(
            text = stringResource(R.string.library_empty_title),
            color = AppTextPrimary,
            style = AppType.headline(26.sp, FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 28.dp),
        )

        Text(
            text = stringResource(R.string.library_empty_subtitle),
            color = AppTextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp, start = 8.dp, end = 8.dp),
        )

        Row(
            modifier =
                Modifier
                    .padding(top = 32.dp)
                    .pressScale(interactionSource)
                    .glow(color = AppPrimary, cornerRadius = 30.dp, alpha = 0.35f)
                    .clip(CircleShape)
                    .background(AppPrimary)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onBrowse,
                    ).padding(horizontal = 28.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(R.string.library_browse_games),
                color = AppOnPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Icon(
                imageVector = AppIcon.FORWARD.image(),
                contentDescription = null,
                tint = AppOnPrimary,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}
