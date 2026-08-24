package com.lucasdias.gametrackr.feature.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.home.components.AnticipatedCard
import com.lucasdias.gametrackr.feature.app.home.components.HomeSectionHeader
import com.lucasdias.gametrackr.feature.app.home.components.NewReleaseCard
import com.lucasdias.gametrackr.feature.app.search.SearchScope
import org.koin.androidx.compose.koinViewModel

private val SectionHeight = 240.dp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onViewAll: (SearchScope) -> Unit = {},
    onGameClick: (String?) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel(),
) {
    val newReleases by viewModel.newReleases.collectAsStateWithLifecycle()
    val mostAnticipated by viewModel.mostAnticipated.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.load() }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(AppBackground)
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        FeedSection(
            title = stringResource(R.string.home_new_releases),
            feed = newReleases,
            onViewAll = { onViewAll(SearchScope.NEW_RELEASES) },
            onRetry = { viewModel.load(force = true) },
        ) { game ->
            NewReleaseCard(game = game, modifier = Modifier.clickable { onGameClick(game.slug) })
        }

        FeedSection(
            title = stringResource(R.string.home_most_anticipated),
            feed = mostAnticipated,
            onViewAll = { onViewAll(SearchScope.MOST_ANTICIPATED) },
            onRetry = { viewModel.load(force = true) },
        ) { game ->
            AnticipatedCard(game = game, modifier = Modifier.clickable { onGameClick(game.slug) })
        }
    }
}

@Composable
private fun FeedSection(
    title: String,
    feed: HomeFeed,
    onViewAll: () -> Unit,
    onRetry: () -> Unit,
    card: @Composable (Game) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        HomeSectionHeader(title = title, onViewAll = onViewAll)
        when {
            feed.games.isEmpty() && (feed.isLoading || !feed.hasLoaded) -> {
                SectionLoading()
            }

            feed.games.isEmpty() -> {
                SectionRetry(
                    message =
                        stringResource(
                            if (feed.hasError) R.string.home_feed_error else R.string.home_feed_empty,
                            title,
                        ),
                    onRetry = onRetry,
                )
            }

            else -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                ) {
                    items(feed.games) { game -> card(game) }
                }
            }
        }
    }
}

@Composable
private fun SectionLoading() {
    Box(
        modifier = Modifier.fillMaxWidth().height(SectionHeight),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = AppPrimary)
    }
}

@Composable
private fun SectionRetry(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().height(SectionHeight).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            color = AppTextSecondary,
            style = AppType.body(14.sp),
            textAlign = TextAlign.Center,
        )
        TextButton(onClick = onRetry) {
            Text(
                text = stringResource(R.string.home_try_again),
                color = AppPrimary,
                style = AppType.label(14.sp),
            )
        }
    }
}
