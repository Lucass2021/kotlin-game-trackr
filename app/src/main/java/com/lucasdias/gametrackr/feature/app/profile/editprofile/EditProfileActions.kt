package com.lucasdias.gametrackr.feature.app.profile.editprofile

import com.lucasdias.gametrackr.core.model.ProfileVisibility

data class EditProfileActions(
    val onBack: () -> Unit = {},
    val onSave: () -> Unit = {},
    val onNameChange: (String) -> Unit = {},
    val onUsernameChange: (String) -> Unit = {},
    val onBioChange: (String) -> Unit = {},
    val onAvatarColorChange: (String) -> Unit = {},
    val onVisibilityChange: (ProfileVisibility) -> Unit = {},
    val onRetryColors: () -> Unit = {},
    val onToastShown: () -> Unit = {},
)
