package com.lucasdias.gametrackr.feature.app.community.createcommunity

data class CreateCommunityActions(
    val onClose: () -> Unit = {},
    val onSubmit: () -> Unit = {},
    val onNameChange: (String) -> Unit = {},
    val onDescriptionChange: (String) -> Unit = {},
    val onToastShown: () -> Unit = {},
)
