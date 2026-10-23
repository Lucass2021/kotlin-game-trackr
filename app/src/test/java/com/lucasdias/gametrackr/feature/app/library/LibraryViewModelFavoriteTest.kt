package com.lucasdias.gametrackr.feature.app.library

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.library.FakeLibraryRepository
import com.lucasdias.gametrackr.data.library.LibraryMockData
import com.lucasdias.gametrackr.data.library.LibraryUpdates
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LibraryViewModelFavoriteTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val slug = LibraryMockData.entries[0].slug

    @Test
    fun `a favorite is shown right away and survives a reload`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = loadedViewModel(FakeLibraryRepository())

            viewModel.onToggleFavorite(slug)
            assertTrue(viewModel.isFavorite(slug))
            advanceUntilIdle()
            viewModel.load()
            advanceUntilIdle()

            assertTrue(viewModel.isFavorite(slug))
        }

    @Test
    fun `a failed favorite is reverted and shows an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeLibraryRepository()
            val viewModel = loadedViewModel(repository)

            repository.failure = IllegalStateException()
            viewModel.onToggleFavorite(slug)
            advanceUntilIdle()

            assertFalse(viewModel.isFavorite(slug))
            assertEquals(R.string.library_favorite_error, viewModel.uiState.value.errorMessage)
        }

    @Test
    fun `a favorite only changes the game with that slug when titles repeat`() =
        runTest(mainDispatcherRule.dispatcher) {
            val original = LibraryMockData.entries[0]
            val twin = original.copy(slug = TWIN_SLUG)
            val viewModel = loadedViewModel(FakeLibraryRepository(entries = listOf(original, twin)))

            viewModel.onToggleFavorite(TWIN_SLUG)
            advanceUntilIdle()

            assertTrue(viewModel.isFavorite(TWIN_SLUG))
            assertFalse(viewModel.isFavorite(original.slug))
        }

    @Test
    fun `a quick second toggle supersedes the first without an error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeLibraryRepository()
            val viewModel = loadedViewModel(repository)

            repository.failure = IllegalStateException()
            viewModel.onToggleFavorite(slug)
            runCurrent()
            repository.failure = null
            viewModel.onToggleFavorite(slug)
            advanceUntilIdle()

            assertEquals(2, repository.setFavoriteCount)
            assertFalse(viewModel.isFavorite(slug))
            assertFalse(repository.isFavorite(slug))
            assertNull(viewModel.uiState.value.errorMessage)
        }

    private fun TestScope.loadedViewModel(repository: FakeLibraryRepository): LibraryViewModel {
        val viewModel = LibraryViewModel(repository, LibraryUpdates())
        viewModel.loadFor(USER_ID)
        advanceUntilIdle()
        return viewModel
    }

    private fun LibraryViewModel.isFavorite(slug: String) =
        uiState.value.entries
            .single { it.slug == slug }
            .isFavorite

    private companion object {
        const val USER_ID = 7
        const val TWIN_SLUG = "cyberpunk-2077-phantom-liberty-2"
    }
}
