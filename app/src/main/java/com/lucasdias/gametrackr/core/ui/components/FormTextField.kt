package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.core.ui.theme.Spacing
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.fieldBox

private val TEXT_SIZE = 16.sp
private val LINE_HEIGHT = 22.sp
private val SINGLE_LINE_HEIGHT = 50.dp
private val DEFAULT_MULTI_LINE_MIN_HEIGHT = 110.dp
private const val DEFAULT_MULTI_LINE_MAX_LINES = 6

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    prefix: String? = null,
    singleLine: Boolean = true,
    multiLineMinHeight: Dp = DEFAULT_MULTI_LINE_MIN_HEIGHT,
    multiLineMaxLines: Int = DEFAULT_MULTI_LINE_MAX_LINES,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = if (singleLine) 1 else multiLineMaxLines,
        keyboardOptions =
            KeyboardOptions(
                capitalization = capitalization,
                imeAction = if (singleLine) ImeAction.Next else ImeAction.Default,
            ),
        textStyle = TextStyle(color = AppTextPrimary, fontSize = TEXT_SIZE, lineHeight = LINE_HEIGHT),
        cursorBrush = SolidColor(AppPrimary),
        modifier =
            modifier
                .fillMaxWidth()
                .fieldBox()
                .then(
                    if (singleLine) {
                        Modifier.height(SINGLE_LINE_HEIGHT).padding(horizontal = Spacing.sm)
                    } else {
                        Modifier.heightIn(min = multiLineMinHeight).padding(Spacing.sm)
                    },
                ),
        decorationBox = { inner ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
            ) {
                if (prefix != null) {
                    Text(text = prefix, color = AppTextSecondary, style = AppType.body(TEXT_SIZE))
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(text = placeholder, color = AppTextSecondary, style = AppType.body(TEXT_SIZE))
                    }
                    inner()
                }
            }
        },
    )
}

@Preview
@Composable
private fun FormTextFieldPreview() {
    AppPreview {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            FormTextField(value = "", onValueChange = {}, placeholder = "How should we call you?")
            FormTextField(value = "lucasdias", onValueChange = {}, placeholder = "", prefix = "@")
            FormTextField(value = "", onValueChange = {}, placeholder = "Tell other players...", singleLine = false)
        }
    }
}
