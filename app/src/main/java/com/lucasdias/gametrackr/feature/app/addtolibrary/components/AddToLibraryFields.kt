package com.lucasdias.gametrackr.feature.app.addtolibrary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Radius
import com.lucasdias.gametrackr.core.ui.theme.Spacing

internal val FIELD_HEIGHT = 50.dp
private val REVIEW_MIN_HEIGHT = 110.dp
private val BORDER_WIDTH = 1.dp
private val LABEL_SIZE = 12.sp
private val LABEL_TRACKING = 0.8.sp
private val INPUT_SIZE = 16.sp
private val REVIEW_LINE_HEIGHT = 22.sp
private val ERROR_SIZE = 13.sp

@Composable
fun AddToLibraryField(
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        SectionLabel(text = label)
        content()
        error?.let { Text(text = it, color = AppTertiary, style = AppType.body(ERROR_SIZE)) }
    }
}

@Composable
fun SectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        color = AppTextPrimary,
        style = AppType.label(LABEL_SIZE, FontWeight.Bold),
        letterSpacing = LABEL_TRACKING,
        modifier = modifier,
    )
}

fun Modifier.fieldBox(isError: Boolean = false): Modifier {
    val shape = RoundedCornerShape(Radius.md)
    return this
        .clip(shape)
        .background(AppSurfaceCard)
        .border(BORDER_WIDTH, if (isError) AppTertiary else AppOutline, shape)
}

@Composable
fun NumberField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        textStyle = AppType.body(INPUT_SIZE).copy(color = AppTextPrimary),
        cursorBrush = SolidColor(AppPrimary),
        modifier =
            modifier
                .fillMaxWidth()
                .fieldBox()
                .height(FIELD_HEIGHT)
                .padding(horizontal = Spacing.sm),
        decorationBox = { inner ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) {
                    Text(text = placeholder, color = AppTextSecondary, style = AppType.body(INPUT_SIZE))
                }
                inner()
            }
        },
    )
}

@Composable
fun ReviewField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = AppType.body(INPUT_SIZE).copy(color = AppTextPrimary, lineHeight = REVIEW_LINE_HEIGHT),
        cursorBrush = SolidColor(AppPrimary),
        modifier =
            modifier
                .fillMaxWidth()
                .fieldBox()
                .heightIn(min = REVIEW_MIN_HEIGHT)
                .padding(Spacing.sm),
        decorationBox = { inner ->
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                if (value.isEmpty()) {
                    Text(text = placeholder, color = AppTextSecondary, style = AppType.body(INPUT_SIZE))
                }
                inner()
            }
        },
    )
}

@Preview
@Composable
private fun AddToLibraryFieldsPreview() {
    AppPreview {
        Column(modifier = Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            AddToLibraryField(label = "Hours played") {
                NumberField(value = "", onValueChange = {}, placeholder = "42")
            }
            AddToLibraryField(label = "Review", error = "Something is missing.") {
                ReviewField(value = "", onValueChange = {}, placeholder = "Write a short review…")
            }
        }
    }
}
