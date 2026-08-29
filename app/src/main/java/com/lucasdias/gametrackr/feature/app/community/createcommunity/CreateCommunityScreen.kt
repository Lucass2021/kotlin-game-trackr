package com.lucasdias.gametrackr.feature.app.community.createcommunity

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.SectionLabel
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.fieldBox
import com.lucasdias.gametrackr.feature.app.community.Community
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateCommunityScreen(
    onBack: () -> Unit,
    onCreated: (Community) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateCommunityViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDiscardConfirm by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun requestClose() {
        if (state.hasContent) showDiscardConfirm = true else onBack()
    }

    BackHandler { requestClose() }

    Column(modifier = modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        Header(
            isSubmitting = state.isSubmitting,
            canSubmit = state.canSubmit,
            onClose = { requestClose() },
            onCreate = {
                scope.launch {
                    val community = viewModel.submit() ?: return@launch
                    onCreated(community)
                }
            },
        )

        Form(state = state, viewModel = viewModel)
    }

    if (showDiscardConfirm) {
        AlertDialog(
            onDismissRequest = { showDiscardConfirm = false },
            containerColor = AppBackground,
            title = {
                Text(
                    text = stringResource(R.string.create_community_discard_title),
                    color = AppTextPrimary,
                    style = AppType.headline(18.sp),
                )
            },
            confirmButton = {
                TextButton(onClick = onBack) {
                    Text(text = stringResource(R.string.create_topic_discard_confirm), color = AppTertiary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardConfirm = false }) {
                    Text(text = stringResource(R.string.create_topic_discard_cancel), color = AppTextSecondary)
                }
            },
        )
    }
}

@Composable
private fun Header(
    isSubmitting: Boolean,
    canSubmit: Boolean,
    onClose: () -> Unit,
    onCreate: () -> Unit,
) {
    val closeInteraction = remember { MutableInteractionSource() }
    val createInteraction = remember { MutableInteractionSource() }
    val closeLabel = stringResource(R.string.community_action_close)

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .pressScale(closeInteraction)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = closeInteraction,
                        indication = null,
                        onClickLabel = closeLabel,
                        role = Role.Button,
                        onClick = onClose,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.CLOSE.image(),
                contentDescription = closeLabel,
                tint = AppTextPrimary,
                modifier = Modifier.size(20.dp),
            )
        }

        Text(
            text = stringResource(R.string.create_community_title),
            color = AppTextPrimary,
            style = AppType.headline(20.sp),
            modifier = Modifier.weight(1f),
        )

        Box(
            modifier =
                Modifier
                    .pressScale(createInteraction)
                    .alpha(if (canSubmit) 1f else 0.45f)
                    .height(38.dp)
                    .clip(CircleShape)
                    .background(AppPrimary)
                    .clickable(
                        interactionSource = createInteraction,
                        indication = null,
                        role = Role.Button,
                        onClick = onCreate,
                    ).padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text =
                    stringResource(
                        if (isSubmitting) {
                            R.string.create_community_submitting
                        } else {
                            R.string.create_community_submit
                        },
                    ),
                color = AppOnPrimary,
                style = AppType.label(15.sp),
            )
        }
    }
}

@Composable
private fun Form(
    state: CreateCommunityUiState,
    viewModel: CreateCommunityViewModel,
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
        Field(
            label = stringResource(R.string.create_community_field_name),
            error = state.nameError,
            hint =
                if (state.isRenamed) {
                    stringResource(R.string.create_community_name_renamed, state.handle)
                } else {
                    stringResource(R.string.create_community_name_hint)
                },
            counter = state.nameRemaining.takeIf { it <= 20 }?.toString(),
            counterIsError = state.nameRemaining < 0,
        ) {
            CommunityTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                placeholder = stringResource(R.string.create_community_name_placeholder),
                singleLine = true,
            )
        }

        Field(
            label = stringResource(R.string.create_community_field_description),
            error = state.descriptionError,
        ) {
            CommunityTextField(
                value = state.description,
                onValueChange = viewModel::onDescriptionChange,
                placeholder = stringResource(R.string.create_community_description_placeholder),
                singleLine = false,
            )
        }

        if (state.submitError != null) {
            Text(
                text = state.submitError,
                color = AppTertiary,
                style = AppType.body(13.sp),
            )
        }
    }
}

@Composable
private fun Field(
    label: String,
    error: Int?,
    hint: String? = null,
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

        when {
            error != null -> {
                Text(text = stringResource(error), color = AppTertiary, style = AppType.body(13.sp))
            }

            hint != null -> {
                Text(text = hint, color = AppTextSecondary, style = AppType.body(13.sp))
            }
        }
    }
}

@Composable
private fun CommunityTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        keyboardOptions =
            KeyboardOptions(
                capitalization = if (singleLine) KeyboardCapitalization.None else KeyboardCapitalization.Sentences,
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
                        Modifier.heightIn(min = 160.dp).padding(14.dp)
                    },
                ),
        decorationBox = { inner ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart,
            ) {
                if (value.isEmpty()) {
                    Text(text = placeholder, color = AppTextSecondary, style = AppType.body(16.sp))
                }
                inner()
            }
        },
    )
}
