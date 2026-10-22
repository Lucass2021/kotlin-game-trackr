package com.lucasdias.gametrackr.feature.app.community.discover

import com.lucasdias.gametrackr.core.model.Community

data class DiscoverCommunitiesActions(
    val onRetry: () -> Unit = {},
    val onLoadMore: () -> Unit = {},
    val onQueryChange: (String) -> Unit = {},
    val onCategoryChange: (String) -> Unit = {},
    val onCommunityClick: (Community) -> Unit = {},
    val onJoinTap: (Community) -> Unit = {},
    val onLeaveConfirmed: () -> Unit = {},
    val onLeaveDismissed: () -> Unit = {},
)
