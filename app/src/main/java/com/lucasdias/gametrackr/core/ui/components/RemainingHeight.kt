package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun rememberRemainingHeight(
    listState: LazyListState,
    key: Any,
): Dp {
    val density = LocalDensity.current
    val remainingPx by remember(listState, key) {
        derivedStateOf {
            val info = listState.layoutInfo
            val item = info.visibleItemsInfo.firstOrNull { it.key == key }
            if (item == null || listState.firstVisibleItemIndex != 0) {
                0
            } else {
                remainingHeightPx(
                    viewportEnd = info.viewportEndOffset,
                    afterContentPadding = info.afterContentPadding,
                    itemOffset = item.offset,
                    scrollOffset = listState.firstVisibleItemScrollOffset,
                )
            }
        }
    }
    return with(density) { remainingPx.toDp() }
}

fun remainingHeightPx(
    viewportEnd: Int,
    afterContentPadding: Int,
    itemOffset: Int,
    scrollOffset: Int,
): Int = (viewportEnd - afterContentPadding - itemOffset - scrollOffset).coerceAtLeast(0)
