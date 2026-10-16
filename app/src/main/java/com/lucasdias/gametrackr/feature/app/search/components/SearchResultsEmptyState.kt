package com.lucasdias.gametrackr.feature.app.search.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.components.EmptyState
import com.lucasdias.gametrackr.core.ui.icon.AppIcon

@Composable
fun SearchResultsEmptyState(
    modifier: Modifier = Modifier,
    query: String = "",
    onClear: (() -> Unit)? = null,
) {
    EmptyState(
        icon = AppIcon.SEARCH,
        title = stringResource(R.string.search_empty_title),
        message =
            if (query.isBlank()) {
                stringResource(R.string.search_empty_filter)
            } else {
                stringResource(R.string.search_empty_query, query)
            },
        actionTitle = stringResource(R.string.search_clear),
        onAction = onClear,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun SearchResultsEmptyStatePreview() {
    AppPreview {
        SearchResultsEmptyState(query = "zelda", onClear = {})
    }
}
