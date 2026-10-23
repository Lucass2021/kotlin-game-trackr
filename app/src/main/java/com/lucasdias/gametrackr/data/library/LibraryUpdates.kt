package com.lucasdias.gametrackr.data.library

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LibraryUpdates {
    private val _addedSlugs = MutableSharedFlow<String>(extraBufferCapacity = BUFFER_SIZE)
    val addedSlugs: SharedFlow<String> = _addedSlugs.asSharedFlow()

    fun publish(slug: String) {
        _addedSlugs.tryEmit(slug)
    }

    private companion object {
        const val BUFFER_SIZE = 16
    }
}
