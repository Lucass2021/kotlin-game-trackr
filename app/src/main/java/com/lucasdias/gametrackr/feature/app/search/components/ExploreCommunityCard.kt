package com.lucasdias.gametrackr.feature.app.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType

@Composable
fun ExploreCommunityCard(
    onExplore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Icon(
            imageVector = AppIcon.BRAND.image(),
            contentDescription = null,
            tint = AppPrimary.copy(alpha = 0.06f),
            modifier = Modifier.padding(end = 24.dp).size(96.dp),
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.search_cant_find_title),
                color = AppTextPrimary,
                style = AppType.headline(20.sp, FontWeight.ExtraBold),
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.search_cant_find_subtitle),
                color = AppTextSecondary,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.search_explore_community),
                color = AppOnPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier =
                    Modifier
                        .padding(top = 4.dp)
                        .pressScale(interactionSource)
                        .clip(CircleShape)
                        .background(AppPrimary)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onExplore,
                        ).padding(horizontal = 24.dp, vertical = 14.dp),
            )
        }
    }
}
