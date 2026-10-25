package com.lucasdias.gametrackr.feature.app.search

import androidx.lifecycle.SavedStateHandle
import com.lucasdias.gametrackr.core.model.Game
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.game.FakeGameRepository
import com.lucasdias.gametrackr.data.game.HomeMockData
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class SearchViewModelSavedStateTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val manyGames = List(SearchUiState.PAGE_SIZE + 5) { Game(id = 100 + it, name = "Game $it", slug = "game-$it") }
    private val repository = FakeGameRepository(searchResults = manyGames)

    @Test
    fun `query and platform survive process death and the first load uses them`() =
        runTest(mainDispatcherRule.dispatcher) {
            val savedState = SavedStateHandle()
            val platform = HomeMockData.platforms.first()
            val before = SearchViewModel(SearchScope.ALL, repository, savedState)
            before.onQueryChange("zelda")
            before.onPlatformChange(platform.slug)
            advanceUntilIdle()
            val restoredRepository = FakeGameRepository(searchResults = manyGames)

            val restored = SearchViewModel(SearchScope.ALL, restoredRepository, savedState)
            restored.loadIfNeeded()
            advanceUntilIdle()

            assertEquals("zelda", restored.uiState.value.query)
            assertEquals(platform.slug, restored.uiState.value.platformSlug)
            assertEquals(1, restoredRepository.searchCount)
            assertEquals("zelda", restoredRepository.lastSearch)
            assertEquals(platform.slug, restoredRepository.lastPlatformSlug)
        }

    @Test
    fun `clearing the filters also clears the saved state`() =
        runTest(mainDispatcherRule.dispatcher) {
            val savedState = SavedStateHandle()
            val before = SearchViewModel(SearchScope.ALL, repository, savedState)
            before.onQueryChange("zelda")
            before.onPlatformChange(HomeMockData.platforms.first().slug)
            before.onClearFilters()
            advanceUntilIdle()

            val restored = SearchViewModel(SearchScope.ALL, repository, savedState)

            assertEquals("", restored.uiState.value.query)
            assertNull(restored.uiState.value.platformSlug)
        }
}
