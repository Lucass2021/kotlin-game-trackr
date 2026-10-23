package com.lucasdias.gametrackr.feature.app.addtolibrary

import com.lucasdias.gametrackr.core.model.LibraryStatus

data class AddToLibraryActions(
    val onStatusChange: (LibraryStatus) -> Unit = {},
    val onRatingChange: (Int) -> Unit = {},
    val onHoursChange: (String) -> Unit = {},
    val onPlatformChange: (String) -> Unit = {},
    val onStartedChange: (Long?) -> Unit = {},
    val onFinishedChange: (Long?) -> Unit = {},
    val onReviewChange: (String) -> Unit = {},
    val onSave: () -> Unit = {},
    val onToastShown: () -> Unit = {},
)
