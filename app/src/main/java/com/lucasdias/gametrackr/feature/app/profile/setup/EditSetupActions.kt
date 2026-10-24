package com.lucasdias.gametrackr.feature.app.profile.setup

data class EditSetupActions(
    val onClose: () -> Unit = {},
    val onSave: () -> Unit = {},
    val onAddPhotos: () -> Unit = {},
    val onDelete: () -> Unit = {},
    val onRemoveImage: (Int) -> Unit = {},
    val onRemoveNewPhoto: (String) -> Unit = {},
    val onTitleChange: (String) -> Unit = {},
    val onDescriptionChange: (String) -> Unit = {},
    val onErrorShown: () -> Unit = {},
)
