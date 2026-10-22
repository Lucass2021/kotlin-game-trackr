package com.lucasdias.gametrackr.feature.app.community

import com.lucasdias.gametrackr.core.model.Community
import com.lucasdias.gametrackr.core.model.CommunityFeedFilter
import com.lucasdias.gametrackr.core.model.CommunityPost

data class CommunityFeedActions(
    val onRefresh: () -> Unit = {},
    val onRetry: () -> Unit = {},
    val onLoadMore: () -> Unit = {},
    val onFilterChange: (CommunityFeedFilter) -> Unit = {},
    val onPostClick: (CommunityPost) -> Unit = {},
    val onLike: (Long) -> Unit = {},
    val onShare: (CommunityPost) -> Unit = {},
    val onBookmark: (Long) -> Unit = {},
    val onCommunityClick: (Community) -> Unit = {},
    val onJoinSuggested: () -> Unit = {},
    val onToastShown: () -> Unit = {},
)
