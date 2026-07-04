package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary

@Composable
fun BackButton(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .offset(x = (-10).dp)
                .size(44.dp)
                .clip(CircleShape)
                .clickable(onClick = onBack),
        contentAlignment = Alignment.CenterStart,
    ) {
        Icon(
            imageVector = AppIcon.BACK.image(),
            contentDescription = stringResource(R.string.register_back),
            tint = AppTextPrimary,
            modifier = Modifier.size(32.dp),
        )
    }
}
