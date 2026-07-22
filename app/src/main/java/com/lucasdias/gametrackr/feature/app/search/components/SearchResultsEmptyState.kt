package com.lucasdias.gametrackr.feature.app.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType

@Composable
fun SearchResultsEmptyState(
    query: String,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier.fillMaxWidth().padding(top = 48.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .subtleBounce()
                    .glow(color = AppPrimary, cornerRadius = 28.dp, blurRadius = 30.dp, alpha = 0.25f)
                    .clip(RoundedCornerShape(28.dp))
                    .background(AppSurfaceCard)
                    .size(120.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.SEARCH.image(),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(52.dp),
            )
        }

        Text(
            text = stringResource(R.string.search_empty_title),
            color = AppTextPrimary,
            style = AppType.headline(24.sp, FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 26.dp),
        )

        Text(
            text =
                if (query.isBlank()) {
                    stringResource(R.string.search_empty_filter)
                } else {
                    stringResource(R.string.search_empty_query, query)
                },
            color = AppTextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp),
        )

        Text(
            text = stringResource(R.string.search_clear),
            color = AppPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier =
                Modifier
                    .padding(top = 22.dp)
                    .pressScale(interactionSource)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClear,
                    ),
        )
    }
}
