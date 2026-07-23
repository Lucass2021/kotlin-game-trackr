package com.lucasdias.gametrackr.feature.app.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.feature.app.notifications.components.NotificationFilterTabs
import com.lucasdias.gametrackr.feature.app.notifications.components.NotificationRow
import com.lucasdias.gametrackr.feature.app.notifications.components.NotificationsEmptyState
import com.lucasdias.gametrackr.feature.app.notifications.components.NotificationsTopBar

@Composable
fun NotificationsScreen(
    isGuest: Boolean,
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
) {
    var filter by rememberSaveable { mutableStateOf(NotificationFilter.ALL) }
    var markedAllRead by rememberSaveable { mutableStateOf(false) }
    val removedIds = remember { mutableListOf<Int>().toMutableStateList() }

    Column(modifier = Modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        NotificationsTopBar(
            onBack = onBack,
            onMarkAllRead = { markedAllRead = true },
            showMarkAllRead = !isGuest,
        )

        if (isGuest) {
            NotificationsEmptyState(
                title = stringResource(R.string.notifications_guest_title),
                subtitle = stringResource(R.string.notifications_guest_subtitle),
                actionText = stringResource(R.string.menu_create_account),
                onAction = onCreateAccount,
            )
        } else {
            val visible =
                NotificationsMockData.items.filter { item ->
                    if (item.id in removedIds) {
                        false
                    } else {
                        when (filter) {
                            NotificationFilter.ALL -> true
                            NotificationFilter.UNREAD -> item.unread && !markedAllRead
                            NotificationFilter.MENTIONS -> item.isMention
                        }
                    }
                }

            NotificationFilterTabs(
                selected = filter,
                onSelect = { filter = it },
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )

            if (visible.isEmpty()) {
                NotificationsEmptyState(
                    title = stringResource(R.string.notifications_empty_title),
                    subtitle = stringResource(R.string.notifications_empty_subtitle),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    NotificationSection.entries.forEach { section ->
                        val sectionItems = visible.filter { it.section == section }
                        if (sectionItems.isNotEmpty()) {
                            item(key = "header_${section.name}") {
                                Text(
                                    text = stringResource(section.title).uppercase(),
                                    color = AppTextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 1.sp,
                                    modifier = Modifier.padding(start = 4.dp, top = 18.dp, bottom = 4.dp),
                                )
                            }
                            items(sectionItems, key = { it.id }) { item ->
                                NotificationRow(
                                    item = item,
                                    markedAllRead = markedAllRead,
                                    onAccept = { removedIds.add(item.id) },
                                    onDecline = { removedIds.add(item.id) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
