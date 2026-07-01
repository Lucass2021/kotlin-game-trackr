package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.theme.AppBackground

@Composable
fun AuthScreenScaffold(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    contentArrangement: Arrangement.Vertical = Arrangement.Top,
    overlay: @Composable BoxScope.() -> Unit = {},
    bottomBar: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier = modifier.fillMaxSize().background(AppBackground)) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .imePadding()
                    .padding(horizontal = 24.dp),
        ) {
            if (onBack != null) {
                BackButton(
                    onBack = onBack,
                    modifier =
                        Modifier
                            .padding(top = 8.dp)
                            .staggeredAppear(0),
                )
            }

            BoxWithConstraints(modifier = Modifier.weight(1f).fillMaxWidth()) {
                val viewportHeight = maxHeight
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .heightIn(min = viewportHeight)
                            .padding(bottom = 16.dp),
                    verticalArrangement = contentArrangement,
                    content = content,
                )
            }

            if (bottomBar != null) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = bottomBar,
                )
            }
        }
        overlay()
    }
}
