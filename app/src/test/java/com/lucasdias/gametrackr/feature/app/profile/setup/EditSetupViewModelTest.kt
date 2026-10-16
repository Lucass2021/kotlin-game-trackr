package com.lucasdias.gametrackr.feature.app.profile.setup

import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.model.SetupImage
import com.lucasdias.gametrackr.core.model.SetupImageUpload
import com.lucasdias.gametrackr.core.model.SetupItem
import com.lucasdias.gametrackr.core.testing.MainDispatcherRule
import com.lucasdias.gametrackr.data.profile.FakeSetupRepository
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EditSetupViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val encoder = SetupPhotoEncoder { SetupImageUpload(bytes = byteArrayOf(1), fileName = "$it.jpg", mimeType = "image/jpeg") }

    @Test
    fun `saving without a title shows the error and does not create the setup`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeSetupRepository(setups = emptyList())
            val viewModel = EditSetupViewModel(null, repository, encoder)
            var saved: SetupItem? = null

            viewModel.onSave { saved = it }
            advanceUntilIdle()

            assertEquals(R.string.setup_title_required, viewModel.uiState.value.titleError)
            assertNull(saved)
            assertTrue(repository.fetchSetups().getOrThrow().isEmpty())
        }

    @Test
    fun `a new setup is created with its photos and the api id`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeSetupRepository(setups = emptyList())
            val viewModel = EditSetupViewModel(null, repository, encoder)
            var saved: SetupItem? = null

            viewModel.onTitleChange("  $TITLE  ")
            viewModel.onPhotosPicked(listOf(PHOTO))
            viewModel.onSave { saved = it }
            advanceUntilIdle()

            val created = repository.fetchSetups().getOrThrow().single()
            assertEquals(created.id, saved?.id)
            assertEquals(TITLE, saved?.title)
            assertEquals(1, saved?.images?.size)
            assertEquals(created.id, viewModel.uiState.value.setupId)
            assertTrue(viewModel.uiState.value.isNew)
            assertTrue(viewModel.uiState.value.isDone)
        }

    @Test
    fun `saving again after a create does not create another setup`() =
        runTest(mainDispatcherRule.dispatcher) {
            val repository = FakeSetupRepository(setups = emptyList())
            val viewModel = EditSetupViewModel(null, repository, encoder)

            viewModel.onTitleChange(TITLE)
            viewModel.onPhotosPicked(listOf(PHOTO))
            viewModel.onSave {}
            advanceUntilIdle()
            viewModel.onSave {}
            advanceUntilIdle()

            val setups = repository.fetchSetups().getOrThrow()
            assertEquals(1, setups.size)
            assertEquals(1, setups.single().images.size)
            assertTrue(viewModel.uiState.value.isDone)
        }

    @Test
    fun `removed photos are sent as removed image ids when updating`() =
        runTest(mainDispatcherRule.dispatcher) {
            val setup = SetupItem(id = SETUP_ID, title = TITLE, description = "", images = listOf(SetupImage(IMAGE_ID, IMAGE_URL)))
            val viewModel = EditSetupViewModel(setup, FakeSetupRepository(setups = listOf(setup)), encoder)
            var saved: SetupItem? = null

            viewModel.onRemoveImage(IMAGE_ID)
            viewModel.onSave { saved = it }
            advanceUntilIdle()

            assertTrue(saved?.images.orEmpty().isEmpty())
            assertTrue(
                viewModel.uiState.value.removedImageIds
                    .isEmpty(),
            )
        }

    @Test
    fun `picked photos never go past the photo limit`() {
        val viewModel = EditSetupViewModel(null, FakeSetupRepository(), encoder)

        viewModel.onPhotosPicked(List(EditSetupUiState.PHOTO_LIMIT + 2) { "$PHOTO$it" })

        assertEquals(EditSetupUiState.PHOTO_LIMIT, viewModel.uiState.value.photoCount)
        assertEquals(0, viewModel.uiState.value.remainingPhotoSlots)
    }

    @Test
    fun `the same photo picked twice is added once`() {
        val viewModel = EditSetupViewModel(null, FakeSetupRepository(), encoder)

        viewModel.onPhotosPicked(listOf(PHOTO, PHOTO))
        viewModel.onPhotosPicked(listOf(PHOTO))

        assertEquals(listOf(PHOTO), viewModel.uiState.value.newPhotos)
    }

    @Test
    fun `a description over the limit shows the error`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = EditSetupViewModel(null, FakeSetupRepository(), encoder)

            viewModel.onTitleChange(TITLE)
            viewModel.onDescriptionChange("a".repeat(EditSetupUiState.DESCRIPTION_LIMIT + 1))
            viewModel.onSave {}

            assertEquals(R.string.setup_description_too_long, viewModel.uiState.value.descriptionError)
        }

    @Test
    fun `a photo that cannot be read shows an error instead of saving`() =
        runTest(mainDispatcherRule.dispatcher) {
            val viewModel = EditSetupViewModel(null, FakeSetupRepository(), { null })
            var saved: SetupItem? = null

            viewModel.onTitleChange(TITLE)
            viewModel.onPhotosPicked(listOf(PHOTO))
            viewModel.onSave { saved = it }
            advanceUntilIdle()

            assertEquals(R.string.setup_photo_error, viewModel.uiState.value.errorMessage)
            assertNull(saved)
        }

    @Test
    fun `deleting returns the setup id`() =
        runTest(mainDispatcherRule.dispatcher) {
            val setup = SetupItem(id = SETUP_ID, title = TITLE, description = "")
            val viewModel = EditSetupViewModel(setup, FakeSetupRepository(setups = listOf(setup)), encoder)
            var deletedId: Int? = null

            viewModel.onDelete { deletedId = it }
            advanceUntilIdle()

            assertEquals(SETUP_ID, deletedId)
        }

    private companion object {
        const val TITLE = "Main Battle Station"
        const val PHOTO = "content://media/photo"
        const val SETUP_ID = 5
        const val IMAGE_ID = 9
        const val IMAGE_URL = "http://10.0.2.2:8000/storage/9/desk.jpg"
    }
}
