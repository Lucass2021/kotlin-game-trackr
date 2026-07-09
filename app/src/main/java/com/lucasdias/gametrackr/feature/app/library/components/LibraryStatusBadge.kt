package com.lucasdias.gametrackr.feature.app.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.feature.app.library.LibraryStatus

@Composable
fun LibraryStatusBadge(
    status: LibraryStatus,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(status.labelRes).uppercase(),
        color = status.badgeForeground,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.6.sp,
        maxLines = 1,
        modifier =
            modifier
                .clip(CircleShape)
                .background(status.badgeBackground)
                .padding(horizontal = 10.dp, vertical = 5.dp),
    )
}
