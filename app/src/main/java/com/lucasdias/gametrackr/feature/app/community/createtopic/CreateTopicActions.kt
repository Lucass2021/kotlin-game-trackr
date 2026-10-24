package com.lucasdias.gametrackr.feature.app.community.createtopic

import com.lucasdias.gametrackr.core.model.Community

data class CreateTopicActions(
    val onClose: () -> Unit = {},
    val onSubmit: () -> Unit = {},
    val onCommunityChange: (Community) -> Unit = {},
    val onTitleChange: (String) -> Unit = {},
    val onBodyChange: (String) -> Unit = {},
    val onRetryCommunities: () -> Unit = {},
    val onCreateAccount: () -> Unit = {},
    val onToastShown: () -> Unit = {},
)
