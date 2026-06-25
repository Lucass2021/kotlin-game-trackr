package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun TitleWithSubtitle(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    centered: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start
    ) {
        Text(
            text = title,
            color = AppPrimary,
            fontSize = 32.sp,
            lineHeight = 38.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = if (centered) TextAlign.Center else TextAlign.Start
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            color = AppTextSecondary,
            fontSize = 17.sp,
            textAlign = if (centered) TextAlign.Center else TextAlign.Start
        )
    }
}
