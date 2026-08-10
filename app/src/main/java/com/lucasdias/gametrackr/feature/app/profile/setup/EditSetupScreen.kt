package com.lucasdias.gametrackr.feature.app.profile.setup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.pressScale
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppBackground
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.SectionLabel
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.fieldBox
import com.lucasdias.gametrackr.feature.app.profile.SetupItem

@Composable
fun EditSetupScreen(
    setup: SetupItem,
    isNew: Boolean,
    onBack: () -> Unit,
    onSave: (SetupItem) -> Unit,
    onDelete: (SetupItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    var title by remember { mutableStateOf(setup.title) }
    var description by remember { mutableStateOf(setup.description) }
    val photos = remember { mutableStateListOf<Uri>().apply { addAll(setup.photos) } }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val canSave = title.isNotBlank()

    val pickPhotos =
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(SETUP_PHOTO_LIMIT),
        ) { picked ->
            photos.addAll(picked.take(SETUP_PHOTO_LIMIT - photos.size))
        }

    Column(modifier = modifier.fillMaxSize().background(AppBackground).statusBarsPadding()) {
        Header(
            isNew = isNew,
            canSave = canSave,
            onBack = onBack,
            onSave = {
                if (!canSave) return@Header
                onSave(
                    setup.copy(
                        title = title.trim(),
                        description = description.trim(),
                        photos = photos.toList(),
                    ),
                )
                onBack()
            },
        )

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
                label = stringResource(R.string.setup_field_photos),
                counter = "${photos.size}/$SETUP_PHOTO_LIMIT",
            ) {
                PhotoStrip(
                    photos = photos,
                    onRemove = { photos.remove(it) },
                    onAdd = {
                        pickPhotos.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                        )
                    },
                )
            }

            Field(label = stringResource(R.string.setup_field_title)) {
                SetupTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = stringResource(R.string.setup_title_placeholder),
                )
            }

            Field(label = stringResource(R.string.setup_field_description)) {
                SetupTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.setup_description_placeholder),
                    singleLine = false,
                )
            }

            if (!isNew) {
                DeleteButton(onClick = { showDeleteConfirm = true })
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            containerColor = AppBackground,
            title = {
                Text(
                    text = stringResource(R.string.setup_delete_title),
                    color = AppTextPrimary,
                    style = AppType.headline(18.sp),
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.setup_delete_message),
                    color = AppTextSecondary,
                    style = AppType.body(15.sp),
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(setup)
                    onBack()
                }) {
                    Text(text = stringResource(R.string.setup_delete_confirm), color = AppTertiary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(
                        text = stringResource(R.string.setup_delete_cancel),
                        color = AppTextSecondary,
                    )
                }
            },
        )
    }
}

@Composable
private fun Header(
    isNew: Boolean,
    canSave: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    val closeInteraction = remember { MutableInteractionSource() }
    val saveInteraction = remember { MutableInteractionSource() }
    val closeLabel = stringResource(R.string.setup_close)

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
                        onClick = onBack,
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
            text = stringResource(if (isNew) R.string.setup_new_title else R.string.setup_edit_title),
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
                text = stringResource(R.string.setup_save),
                color = AppOnPrimary,
                style = AppType.label(15.sp),
            )
        }
    }
}

@Composable
private fun PhotoStrip(
    photos: List<Uri>,
    onRemove: (Uri) -> Unit,
    onAdd: () -> Unit,
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()).padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        photos.forEach { uri ->
            Thumbnail(uri = uri, onRemove = { onRemove(uri) })
        }

        if (photos.size < SETUP_PHOTO_LIMIT) {
            AddPhotoTile(onClick = onAdd)
        }
    }
}

@Composable
private fun Thumbnail(
    uri: Uri,
    onRemove: () -> Unit,
) {
    val removeInteraction = remember { MutableInteractionSource() }
    val removeLabel = stringResource(R.string.setup_remove_photo)

    Box(modifier = Modifier.size(108.dp)) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppSurfaceCard),
        ) {
            rememberSetupPhoto(uri)?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .pressScale(removeInteraction)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(
                        interactionSource = removeInteraction,
                        indication = null,
                        onClickLabel = removeLabel,
                        role = Role.Button,
                        onClick = onRemove,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AppIcon.CLOSE.image(),
                contentDescription = removeLabel,
                tint = Color.White,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

@Composable
private fun AddPhotoTile(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(14.dp)
    val label = stringResource(R.string.setup_add_photo)

    Column(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .size(108.dp)
                .clip(shape)
                .background(AppSurfaceCard)
                .border(1.dp, AppOutline, shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onClick,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = AppIcon.PLUS.image(),
            contentDescription = null,
            tint = AppPrimary,
            modifier = Modifier.size(22.dp),
        )
        Text(
            text = label,
            color = AppTextSecondary,
            style = AppType.body(12.sp),
        )
    }
}

@Composable
private fun DeleteButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(14.dp)
    val label = stringResource(R.string.setup_delete)

    Row(
        modifier =
            Modifier
                .pressScale(interactionSource)
                .fillMaxWidth()
                .height(52.dp)
                .clip(shape)
                .border(1.dp, AppTertiary.copy(alpha = 0.4f), shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = label,
                    role = Role.Button,
                    onClick = onClick,
                ),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = AppIcon.TRASH.image(),
            contentDescription = null,
            tint = AppTertiary,
            modifier = Modifier.size(18.dp),
        )
        Text(text = label, color = AppTertiary, style = AppType.label(16.sp))
    }
}

@Composable
private fun Field(
    label: String,
    counter: String? = null,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SectionLabel(text = label, modifier = Modifier.weight(1f))
            if (counter != null) {
                Text(text = counter, color = AppTextSecondary, style = AppType.body(12.sp))
            }
        }

        content()
    }
}

@Composable
private fun SetupTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        keyboardOptions =
            KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
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
