package com.lucasdias.gametrackr.feature.app.library

import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.model.NewLibraryEntry
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletEnd
import com.lucasdias.gametrackr.core.ui.theme.CoverVioletStart
import com.lucasdias.gametrackr.data.library.FakeLibraryRepository
import com.lucasdias.gametrackr.data.library.LibraryUpdates
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LibraryViewModelUpdatesTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeLibraryRepository()
    private val libraryUpdates = LibraryUpdates()

    @Test
    fun `a game added from its detail screen shows up in the library`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = LibraryViewModel(repository, libraryUpdates)
            viewModel.loadFor(USER_ID)
            advanceUntilIdle()

            repository.addToLibrary(newEntry)
            libraryUpdates.publish(newEntry.slug)
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.entries
                    .any { it.slug == newEntry.slug },
            )
        }

    @Test
    fun `a signed out library ignores added games`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = LibraryViewModel(repository, libraryUpdates)
            advanceUntilIdle()

            libraryUpdates.publish(newEntry.slug)
            advanceUntilIdle()

            assertEquals(0, repository.fetchCount)
        }

    private companion object {
        const val USER_ID = 1
        val newEntry =
            NewLibraryEntry(
                title = "Neon Ascent: Revival",
                slug = "neon-ascent-revival",
                status = LibraryStatus.PLAYING,
                rating = 0,
                hours = null,
                platform = "PS5",
                startedAt = null,
                finishedAt = null,
                review = null,
                coverStart = CoverVioletStart,
                coverEnd = CoverVioletEnd,
            )
    }
}
