package com.lucasdias.gametrackr.feature.app.addtolibrary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun SectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        color = AppTextPrimary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        modifier = modifier,
    )
}

fun Modifier.fieldBox(): Modifier {
    val shape = RoundedCornerShape(12.dp)
    return this
        .clip(shape)
        .background(AppSurfaceCard)
        .border(1.dp, AppOutline, shape)
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
        onValueChange = { new -> onValueChange(new.filter { it.isDigit() }) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        textStyle = TextStyle(color = AppTextPrimary, fontSize = 16.sp),
        cursorBrush =
            androidx.compose.ui.graphics
                .SolidColor(AppPrimary),
        modifier =
            modifier
                .fillMaxWidth()
                .fieldBox()
                .height(50.dp)
                .padding(horizontal = 14.dp),
        decorationBox = { inner ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) {
                    Text(text = placeholder, color = AppTextSecondary, fontSize = 16.sp)
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
        textStyle = TextStyle(color = AppTextPrimary, fontSize = 16.sp, lineHeight = 22.sp),
        cursorBrush =
            androidx.compose.ui.graphics
                .SolidColor(AppPrimary),
        modifier =
            modifier
                .fillMaxWidth()
                .fieldBox()
                .heightIn(min = 110.dp)
                .padding(14.dp),
        decorationBox = { inner ->
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                if (value.isEmpty()) {
                    Text(text = placeholder, color = AppTextSecondary, fontSize = 16.sp)
                }
                inner()
            }
        },
    )
}
