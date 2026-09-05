package com.lucasdias.gametrackr.feature.app.home

import app.cash.turbine.test
import com.lucasdias.gametrackr.support.FakeGameApi
import com.lucasdias.gametrackr.support.MainDispatcherRule
import com.lucasdias.gametrackr.support.TestData
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loads both sliders from the injected api`() =
        runTest {
            val api = FakeGameApi(slider = listOf(TestData.game(id = 1, name = "Hades II")))
            val viewModel = HomeViewModel(api)

            viewModel.load()

            assertEquals(
                listOf("Hades II"),
                viewModel.newReleases.value.games
                    .map { it.name },
            )
            assertEquals(
                listOf("Hades II"),
                viewModel.mostAnticipated.value.games
                    .map { it.name },
            )
            assertTrue(viewModel.newReleases.value.hasLoaded)
            assertFalse(viewModel.newReleases.value.isLoading)
        }

    @Test
    fun `emits loading before settling into the loaded state`() =
        runTest {
            val gate = CompletableDeferred<Unit>()
            val api =
                FakeGameApi(
                    slider = listOf(TestData.game(id = 1, name = "Hades II")),
                    gate = gate,
                )
            val viewModel = HomeViewModel(api)

            viewModel.newReleases.test {
                assertFalse(awaitItem().hasLoaded)
                viewModel.load()
                assertTrue(awaitItem().isLoading)
                gate.complete(Unit)
                assertEquals(1, awaitItem().games.size)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `flags an error without losing the settled state`() =
        runTest {
            val viewModel = HomeViewModel(FakeGameApi(failure = IOException("offline")))

            viewModel.load()

            assertTrue(viewModel.newReleases.value.hasError)
            assertTrue(viewModel.newReleases.value.hasLoaded)
            assertTrue(
                viewModel.newReleases.value.games
                    .isEmpty(),
            )
        }

    @Test
    fun `skips the refetch unless forced`() =
        runTest {
            val api = FakeGameApi(slider = listOf(TestData.game(id = 1, name = "Hades II")))
            val viewModel = HomeViewModel(api)

            viewModel.load()
            viewModel.load()

            assertEquals(1, api.callCount("newReleases"))

            viewModel.load(force = true)

            assertEquals(2, api.callCount("newReleases"))
        }
}
