package com.lucasdias.gametrackr.feature.app.notifications

import androidx.annotation.StringRes
import com.lucasdias.gametrackr.core.model.NotificationFilter
import com.lucasdias.gametrackr.core.model.NotificationItem
import com.lucasdias.gametrackr.core.model.NotificationSection

data class NotificationSectionGroup(
    val section: NotificationSection,
    val items: List<NotificationItem>,
)

data class NotificationsUiState(
    val items: List<NotificationItem> = emptyList(),
    val filter: NotificationFilter = NotificationFilter.ALL,
    val currentPage: Int = 0,
    val lastPage: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasError: Boolean = false,
    val loadMoreFailed: Boolean = false,
    @param:StringRes val errorMessage: Int? = null,
) {
    val canLoadMore: Boolean
        get() = currentPage in 1 until lastPage && !isLoading && !isLoadingMore && !loadMoreFailed

    val sections: List<NotificationSectionGroup>
        get() =
            NotificationSection.entries.mapNotNull { section ->
                val sectionItems = items.filter { it.section == section }
                if (sectionItems.isEmpty()) null else NotificationSectionGroup(section, sectionItems)
            }

    companion object {
        const val PAGE_SIZE = 4
    }
}
