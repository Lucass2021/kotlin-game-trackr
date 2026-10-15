package com.lucasdias.gametrackr.feature.app.community

import androidx.compose.ui.graphics.Color

enum class CommunitySegment(
    val label: String,
) {
    MY_FEED("Feed"),
    DISCOVER("Discover"),
}

enum class CommunityFeedFilter(
    val label: String,
) {
    LATEST("Latest"),
    TOP_RATED("Top Rated"),
    FOLLOWING("Following"),
}

enum class CommunityDetailTab(
    val label: String,
) {
    POSTS("Posts"),
    ABOUT("About"),
    MEMBERS("Members"),
}
