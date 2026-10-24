package com.lucasdias.gametrackr.feature.app.help.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

private val ICON_SIZE = 48.dp
private val TITLE_SIZE = 22.sp
private val SUBTITLE_SIZE = 15.sp

@Composable
fun HelpHero(modifier: Modifier = Modifier) {
    Surface(color = AppSurfaceCard, shape = RoundedCornerShape(Radius.lg), modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.xl, horizontal = Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Icon(
                imageVector = AppIcon.HELP.image(filled = true),
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(ICON_SIZE),
            )
            Text(text = stringResource(R.string.help_hero_title), color = AppTextPrimary, style = AppType.headline(TITLE_SIZE))
            Text(
                text = stringResource(R.string.help_hero_subtitle),
                color = AppTextSecondary,
                style = AppType.body(SUBTITLE_SIZE),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun HelpHeroPreview() {
    AppPreview {
        HelpHero()
    }
}
