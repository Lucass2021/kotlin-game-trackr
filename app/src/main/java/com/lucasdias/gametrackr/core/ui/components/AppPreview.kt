package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme

@Composable
fun AppPreview(content: @Composable () -> Unit) {
    GameTrackrTheme {
        Box(modifier = Modifier.background(AppBackground)) {
            content()
        }
    }
}
