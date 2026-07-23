package com.lucasdias.gametrackr.feature.app.community.createtopic

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
import com.lucasdias.gametrackr.feature.app.community.CommunityPost
import com.lucasdias.gametrackr.feature.app.community.components.CommunityEmptyState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CreateTopicScreen(
    isGuest: Boolean,
    communityName: String,
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    onPost: (CommunityPost) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateTopicViewModel = koinViewModel { parametersOf(communityName) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDiscardConfirm by remember { mutableStateOf(false) }

    fun requestClose() {
        if (state.hasContent) showDiscardConfirm = true else onBack()
    }

    BackHandler { requestClose() }

    Column(modifier = modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        Header(
            showPostAction = !isGuest,
            canSubmit = state.canSubmit,
            onClose = { requestClose() },
            onPost = {
                val post = viewModel.onSubmit() ?: return@Header
                onPost(post)
                onBack()
            },
        )

        if (isGuest) {
            CommunityEmptyState(
                icon = AppIcon.COMMUNITY,
                title = stringResource(R.string.create_topic_guest_title),
                message = stringResource(R.string.create_topic_guest_message),
                actionTitle = stringResource(R.string.create_topic_guest_action),
                onAction = onCreateAccount,
            )
        } else {
            Form(state = state, viewModel = viewModel)
        }
    }

    if (showDiscardConfirm) {
        AlertDialog(
            onDismissRequest = { showDiscardConfirm = false },
            containerColor = AppBackground,
            title = {
                Text(
                    text = stringResource(R.string.create_topic_discard_title),
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
    showPostAction: Boolean,
    canSubmit: Boolean,
    onClose: () -> Unit,
    onPost: () -> Unit,
) {
    val closeInteraction = remember { MutableInteractionSource() }
    val postInteraction = remember { MutableInteractionSource() }
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
            text = stringResource(R.string.create_topic_title),
            color = AppTextPrimary,
            style = AppType.headline(20.sp),
            modifier = Modifier.weight(1f),
        )

        if (showPostAction) {
            Box(
                modifier =
                    Modifier
                        .pressScale(postInteraction)
                        .alpha(if (canSubmit) 1f else 0.45f)
                        .height(38.dp)
                        .clip(CircleShape)
                        .background(AppPrimary)
                        .clickable(
                            interactionSource = postInteraction,
                            indication = null,
                            role = Role.Button,
                            onClick = onPost,
                        ).padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.create_topic_submit),
                    color = AppOnPrimary,
                    style = AppType.label(15.sp),
                )
            }
        }
    }
}

@Composable
private fun Form(
    state: CreateTopicUiState,
    viewModel: CreateTopicViewModel,
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
            label = stringResource(R.string.create_topic_field_community),
            error = state.communityError,
        ) {
            CommunityPickerField(
                selection = state.community,
                options = viewModel.communities,
                onSelect = viewModel::onCommunityChange,
                isLocked = state.isCommunityLocked,
            )
        }

        Field(
            label = stringResource(R.string.create_topic_field_title),
            error = state.titleError,
            counter = state.titleRemaining.takeIf { it <= 20 }?.toString(),
            counterIsError = state.titleRemaining < 0,
        ) {
            TopicTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                placeholder = stringResource(R.string.create_topic_title_placeholder),
                singleLine = true,
            )
        }

        Field(
            label = stringResource(R.string.create_topic_field_body),
            error = state.bodyError,
        ) {
            TopicTextField(
                value = state.body,
                onValueChange = viewModel::onBodyChange,
                placeholder = stringResource(R.string.create_topic_body_placeholder),
                singleLine = false,
            )
        }
    }
}

@Composable
private fun Field(
    label: String,
    error: Int?,
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
private fun TopicTextField(
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
            KeyboardOptions(imeAction = if (singleLine) ImeAction.Next else ImeAction.Default),
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
