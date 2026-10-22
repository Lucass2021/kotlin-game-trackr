package com.lucasdias.gametrackr.feature.app.community

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R

enum class CommunitySegment(
    @param:StringRes val labelRes: Int,
) {
    MY_FEED(R.string.community_segment_feed),
    DISCOVER(R.string.community_segment_discover),
}

enum class CommunityDetailTab(
    @param:StringRes val labelRes: Int,
) {
    POSTS(R.string.community_detail_tab_posts),
    ABOUT(R.string.community_detail_tab_about),
    MEMBERS(R.string.community_detail_tab_members),
}
