package com.lucasdias.gametrackr.feature.app.addtolibrary

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.LibraryStatus
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.game.FakeGameRepository
import com.lucasdias.gametrackr.data.library.FakeLibraryRepository
import com.lucasdias.gametrackr.data.library.LibraryUpdates
import com.lucasdias.gametrackr.feature.app.library.LibraryViewModel
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AddToLibraryViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val game = FakeGameRepository.gameDetail
    private val repository = FakeLibraryRepository(entries = emptyList())
    private val libraryUpdates = LibraryUpdates()

    private fun viewModel(platforms: List<String> = game.platforms) = AddToLibraryViewModel(repository, libraryUpdates, SLUG, game.copy(platforms = platforms))

    @Test
    fun `saving adds the game to the library and tells the library screen`() =
        runTest(mainDispatcherRule.dispatcher) {
            val library = LibraryViewModel(repository, libraryUpdates)
            library.loadFor(USER_ID)
            advanceUntilIdle()
            val viewModel = viewModel()

            viewModel.onStatusChange(LibraryStatus.COMPLETED)
            viewModel.onRatingChange(RATING)
            viewModel.onHoursChange(HOURS)
            viewModel.onSave()
            advanceUntilIdle()

            val entry = repository.entry(SLUG)
            assertEquals(LibraryStatus.COMPLETED, entry?.status)
            assertEquals(RATING, entry?.rating)
            assertEquals(HOURS.toInt(), entry?.hours)
            assertEquals(game.title, entry?.title)
            assertTrue(viewModel.uiState.value.isSaved)
            assertEquals(
                listOf(SLUG),
                library.uiState.value.entries
                    .map { it.slug },
            )
        }

    @Test
    fun `the platform choices come from the game`() {
        val viewModel = viewModel(platforms = listOf("Switch", "PC"))

        assertEquals(listOf("Switch", "PC"), viewModel.uiState.value.platforms)
        assertEquals("Switch", viewModel.uiState.value.platform)
    }

    @Test
    fun `a game without platforms has no platform to pick`() {
        val viewModel = viewModel(platforms = emptyList())

        assertNull(viewModel.uiState.value.platform)
    }

    @Test
    fun `a finish date before the start date shows the error and does not save`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.onStartedChange(LATER)
            viewModel.onFinishedChange(EARLIER)

            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(R.string.add_library_date_error, viewModel.uiState.value.dateError)
            assertEquals(0, repository.addCount)
        }

    @Test
    fun `the date error only shows after trying to save`() {
        val viewModel = viewModel()
        viewModel.onStartedChange(LATER)
        viewModel.onFinishedChange(EARLIER)

        assertNull(viewModel.uiState.value.dateError)
    }

    @Test
    fun `a save failure keeps the form and shows the toast`() =
        runTest(mainDispatcherRule.dispatcher) {
            repository.failure = IllegalStateException()
            val viewModel = viewModel()
            viewModel.onReviewChange(REVIEW)

            viewModel.onSave()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(R.string.add_library_save_error, state.toastMessage)
            assertEquals(REVIEW, state.review)
            assertFalse(state.isSaved)
            assertFalse(state.isSaving)
        }

    @Test
    fun `a double tap on save adds the game once`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()

            viewModel.onSave()
            runCurrent()
            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(1, repository.addCount)
        }

    @Test
    fun `hours keep only digits up to five characters`() {
        val viewModel = viewModel()

        viewModel.onHoursChange("12a34567")

        assertEquals("12345", viewModel.uiState.value.hours)
    }

    @Test
    fun `hours ignore number symbols that are not plain digits`() {
        val viewModel = viewModel()

        viewModel.onHoursChange("1\u00BD2\u0663")

        assertEquals("12", viewModel.uiState.value.hours)
    }

    @Test
    fun `reopening after a save starts a fresh form`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = viewModel()
            viewModel.onRatingChange(RATING)
            viewModel.onSave()
            advanceUntilIdle()

            viewModel.reset()

            val state = viewModel.uiState.value
            assertEquals(0, state.rating)
            assertFalse(state.isSaved)
            assertEquals(game.platforms.first(), state.platform)
        }

    private companion object {
        const val SLUG = "neon-ascent-revival"
        const val USER_ID = 1
        const val RATING = 4
        const val HOURS = "42"
        const val REVIEW = "Great climb."
        const val EARLIER = 1_709_251_200_000L
        const val LATER = 1_710_417_600_000L
    }
}
