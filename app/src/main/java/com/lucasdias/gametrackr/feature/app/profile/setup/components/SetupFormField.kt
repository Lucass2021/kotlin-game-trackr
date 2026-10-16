package com.lucasdias.gametrackr.feature.app.profile.setup.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.components.AppPreview
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppType
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.SectionLabel
import com.lucasdias.gametrackr.feature.app.addtolibrary.components.fieldBox

@Composable
fun SetupFormField(
    label: String,
    modifier: Modifier = Modifier,
    counter: String? = null,
    error: Int? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SectionLabel(text = label, modifier = Modifier.weight(1f))
            if (counter != null) {
                Text(text = counter, color = AppTextSecondary, style = AppType.body(12.sp))
            }
        }

        content()

        if (error != null) {
            Text(text = stringResource(error), color = AppTertiary, style = AppType.body(13.sp))
        }
    }
}

@Composable
fun SetupTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
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
            modifier
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

@Preview
@Composable
private fun SetupFormFieldPreview() {
    AppPreview {
        SetupFormField(
            label = "Title",
            error = R.string.setup_title_required,
            modifier = Modifier.padding(20.dp),
        ) {
            SetupTextField(value = "", onValueChange = {}, placeholder = "Main Battle Station")
        }
    }
}
