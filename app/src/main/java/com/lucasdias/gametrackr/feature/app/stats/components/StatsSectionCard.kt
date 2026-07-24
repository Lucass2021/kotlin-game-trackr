package com.lucasdias.gametrackr.feature.app.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppType

@Composable
fun StatsSectionCard(
    title: String,
    icon: AppIcon,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(18.dp)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = title,
                color = AppTextPrimary,
                style = AppType.headline(22.sp, FontWeight.ExtraBold),
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = icon.image(),
                contentDescription = null,
                tint = AppSecondary,
                modifier = Modifier.size(22.dp),
            )
        }

        content()
    }
}
