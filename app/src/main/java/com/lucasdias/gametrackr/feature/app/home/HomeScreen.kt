package com.lucasdias.gametrackr.feature.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.feature.app.home.components.AnticipatedCard
import com.lucasdias.gametrackr.feature.app.home.components.HomeSectionHeader
import com.lucasdias.gametrackr.feature.app.home.components.NewReleaseCard

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(AppBackground)
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        NewReleasesSection()
        MostAnticipatedSection()
    }
}

@Composable
private fun NewReleasesSection() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        HomeSectionHeader(title = stringResource(R.string.home_new_releases), onViewAll = {})
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
        ) {
            items(HomeMockData.newReleases) { release ->
                NewReleaseCard(release = release)
            }
        }
    }
}

@Composable
private fun MostAnticipatedSection() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        HomeSectionHeader(title = stringResource(R.string.home_most_anticipated), onViewAll = {})
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
        ) {
            items(HomeMockData.mostAnticipated) { game ->
                AnticipatedCard(game = game)
            }
        }
    }
}
