package com.lucasdias.gametrackr.feature.app.search

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.R

enum class SearchScope(
    @param:StringRes val titleRes: Int,
) {
    ALL(R.string.search_scope_all),
    NEW_RELEASES(R.string.home_new_releases),
    MOST_ANTICIPATED(R.string.home_most_anticipated),
    ;

    val isFiltered: Boolean get() = this != ALL
}
