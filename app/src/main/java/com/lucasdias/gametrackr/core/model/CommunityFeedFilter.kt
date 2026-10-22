package com.lucasdias.gametrackr.core.model

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R

enum class CommunityFeedFilter(
    @param:StringRes val labelRes: Int,
) {
    LATEST(R.string.community_filter_latest),
    TOP_RATED(R.string.community_filter_top_rated),
    FOLLOWING(R.string.community_filter_following),
}
