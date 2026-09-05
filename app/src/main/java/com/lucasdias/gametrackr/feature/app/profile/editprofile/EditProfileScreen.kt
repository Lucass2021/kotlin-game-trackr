package com.lucasdias.gametrackr.feature.app.profile.editprofile

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.Toast
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.darkened
import com.lucasdias.gametrackr.core.ui.theme.toAppColor
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.SectionLabel
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.fieldBox
import com.lucasdias.gametrackr.feature.app.community.components.CommunityAvatar
import com.lucasdias.gametrackr.feature.app.profile.Profile
import com.lucasdias.gametrackr.feature.app.profile.editprofile.components.AvatarColorPicker
import com.lucasdias.gametrackr.feature.app.profile.editprofile.components.VisibilitySelector
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val BIO_COUNTER_THRESHOLD = 40

@Composable
fun EditProfileScreen(
    profile: Profile,
    onBack: () -> Unit,
    onSave: (Profile) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = koinViewModel { parametersOf(profile) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDiscardConfirm by remember { mutableStateOf(false) }

    fun requestClose() {
        if (viewModel.hasChanges()) showDiscardConfirm = true else onBack()
    }

    BackHandler { requestClose() }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
            Header(
                canSave = state.canSave && !state.isSaving,
                onBack = { requestClose() },
                onSave = {
                    viewModel.onSave { updated ->
                        onSave(updated)
                        onBack()
                    }
                },
            )

            Form(state = state, viewModel = viewModel, original = profile)
        }

        Toast(
            message = state.errorMessage,
            onDismiss = viewModel::onErrorShown,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }

    if (showDiscardConfirm) {
        AlertDialog(
            onDismissRequest = { showDiscardConfirm = false },
            containerColor = AppBackground,
            title = {
                Text(
                    text = stringResource(R.string.edit_profile_discard_title),
                    color = AppTextPrimary,
                    style = AppType.headline(18.sp),
                )
            },
            confirmButton = {
                TextButton(onClick = onBack) {
                    Text(text = stringResource(R.string.edit_profile_discard_confirm), color = AppTertiary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardConfirm = false }) {
                    Text(
                        text = stringResource(R.string.edit_profile_discard_cancel),
                        color = AppTextSecondary,
                    )
                }
            },
        )
    }
}

@Composable
private fun Header(
    canSave: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    val backInteraction = remember { MutableInteractionSource() }
    val saveInteraction = remember { MutableInteractionSource() }
    val backLabel = stringResource(R.string.edit_profile_back)

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .pressScale(backInteraction)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = backInteraction,
                        indication = null,
                        onClickLabel = backLabel,
                        role = Role.Button,
                        onClick = onBack,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.BACK.image(),
                contentDescription = backLabel,
                tint = AppTextPrimary,
                modifier = Modifier.size(22.dp),
            )
        }

        Text(
            text = stringResource(R.string.edit_profile_title),
            color = AppTextPrimary,
            style = AppType.headline(20.sp),
            modifier = Modifier.weight(1f),
        )

        Box(
            modifier =
                Modifier
                    .pressScale(saveInteraction)
                    .alpha(if (canSave) 1f else 0.45f)
                    .height(38.dp)
                    .clip(CircleShape)
                    .background(AppPrimary)
                    .clickable(
                        interactionSource = saveInteraction,
                        indication = null,
                        role = Role.Button,
                        onClick = onSave,
                    ).padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.edit_profile_save),
                color = AppOnPrimary,
                style = AppType.label(15.sp),
            )
        }
    }
}

@Composable
private fun Form(
    state: EditProfileUiState,
    viewModel: EditProfileViewModel,
    original: Profile,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        LivePreview(state = state, original = original)

        Field(label = stringResource(R.string.edit_profile_field_avatar)) {
            AvatarColorPicker(
                colors = state.colors,
                selection = state.avatarHex,
                onSelect = viewModel::onAvatarColorChange,
            )
        }

        Field(
            label = stringResource(R.string.edit_profile_field_name),
            error = state.nameError,
        ) {
            ProfileTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                placeholder = stringResource(R.string.edit_profile_name_placeholder),
            )
        }

        Field(
            label = stringResource(R.string.edit_profile_field_username),
            error = state.usernameError,
        ) {
            ProfileTextField(
                value = state.username,
                onValueChange = viewModel::onUsernameChange,
                placeholder = stringResource(R.string.edit_profile_username_placeholder),
                prefix = stringResource(R.string.edit_profile_username_prefix),
                capitalization = KeyboardCapitalization.None,
            )
        }

        Field(
            label = stringResource(R.string.edit_profile_field_bio),
            error = state.bioError,
            counter = state.bioRemaining.takeIf { it <= BIO_COUNTER_THRESHOLD }?.toString(),
            counterIsError = state.bioRemaining < 0,
        ) {
            ProfileTextField(
                value = state.bio,
                onValueChange = viewModel::onBioChange,
                placeholder = stringResource(R.string.edit_profile_bio_placeholder),
                singleLine = false,
            )
        }

        Field(label = stringResource(R.string.edit_profile_field_visibility)) {
            VisibilitySelector(selection = state.visibility, onSelect = viewModel::onVisibilityChange)
        }
    }
}

@Composable
private fun LivePreview(
    state: EditProfileUiState,
    original: Profile,
) {
    val start by animateColorAsState(state.avatarHex.toAppColor(), label = "avatarStart")
    val end by animateColorAsState(state.avatarHex.toAppColor().darkened(AVATAR_GRADIENT_DEPTH), label = "avatarEnd")

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(AppSurfaceCard)
                .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CommunityAvatar(start = start, end = end, size = 64.dp)

        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = state.name.trim().ifBlank { original.name },
                color = AppTextPrimary,
                style = AppType.headline(18.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = state.normalizedUsername.ifBlank { original.username.removePrefix("@") }.let { "@$it" },
                color = AppPrimary,
                style = AppType.body(14.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun Field(
    label: String,
    error: Int? = null,
    counter: String? = null,
    counterIsError: Boolean = false,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SectionLabel(text = label, modifier = Modifier.weight(1f))
            if (counter != null) {
                Text(
                    text = counter,
                    color = if (counterIsError) AppTertiary else AppTextSecondary,
                    style = AppType.body(12.sp),
                )
            }
        }

        content()

        if (error != null) {
            Text(
                text = stringResource(error),
                color = AppTertiary,
                style = AppType.body(13.sp),
            )
        }
    }
}

@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    prefix: String? = null,
    singleLine: Boolean = true,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        keyboardOptions =
            KeyboardOptions(
                capitalization = capitalization,
                imeAction = if (singleLine) ImeAction.Next else ImeAction.Default,
            ),
        textStyle = TextStyle(color = AppTextPrimary, fontSize = 16.sp, lineHeight = 22.sp),
        cursorBrush = SolidColor(AppPrimary),
        modifier =
            Modifier
                .fillMaxWidth()
                .fieldBox()
                .then(
                    if (singleLine) {
                        Modifier.height(50.dp).padding(horizontal = 14.dp)
                    } else {
                        Modifier.heightIn(min = 110.dp).padding(14.dp)
                    },
                ),
        decorationBox = { inner ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
            ) {
                if (prefix != null) {
                    Text(text = prefix, color = AppTextSecondary, style = AppType.body(16.sp))
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(text = placeholder, color = AppTextSecondary, style = AppType.body(16.sp))
                    }
                    inner()
                }
            }
        },
    )
}

private const val AVATAR_GRADIENT_DEPTH = 0.28f
