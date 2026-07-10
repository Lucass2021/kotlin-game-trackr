package com.lucasdias.gametrackr.feature.app.gamedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun GameInfoChip(
    text: String,
    modifier: Modifier = Modifier,
    isRating: Boolean = false,
) {
    val shape = RoundedCornerShape(10.dp)
    Row(
        modifier =
            modifier
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .padding(horizontal = 12.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isRating) {
            Text(text = "★", color = AppPrimary, fontSize = 13.sp)
        }
        Text(
            text = text,
            color = if (isRating) AppPrimary else AppTextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun GameGenreChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = AppPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier =
            modifier
                .clip(CircleShape)
                .background(AppPrimary.copy(alpha = 0.12f))
                .border(1.dp, AppPrimary.copy(alpha = 0.45f), CircleShape)
                .padding(horizontal = 14.dp, vertical = 8.dp),
    )
}
