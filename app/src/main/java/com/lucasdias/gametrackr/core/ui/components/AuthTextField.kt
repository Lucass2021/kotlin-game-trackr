package com.lucasdias.gametrackr.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSurfaceCard
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary
import com.lucasdias.gametrackr.core.ui.theme.AppTextPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary

@Composable
fun AuthTextField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    @StringRes error: Int? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    var revealed by remember { mutableStateOf(false) }

    val borderColor = when {
        error != null -> AppTertiary
        isFocused -> AppPrimary
        else -> AppOutline
    }
    val borderWidth = if (error != null || isFocused) 2.dp else 1.dp
    val shape = RoundedCornerShape(14.dp)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = AppTextSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(10.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(color = AppTextPrimary, fontSize = 16.sp),
            cursorBrush = SolidColor(AppPrimary),
            visualTransformation = if (isPassword && !revealed) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            interactionSource = interactionSource,
            modifier = Modifier.fillMaxWidth()
        ) { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(shape)
                    .background(AppSurfaceCard)
                    .border(borderWidth, borderColor, shape)
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(text = placeholder, color = AppTextSecondary, fontSize = 16.sp)
                    }
                    innerTextField()
                }
                if (isPassword) {
                    val icon = if (revealed) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = stringResource(
                        if (revealed) R.string.register_hide_password else R.string.register_show_password
                    )
                    Icon(
                        imageVector = icon,
                        contentDescription = description,
                        tint = AppTextSecondary,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { revealed = !revealed }
                    )
                }
            }
        }
        if (error != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(error),
                color = AppTertiary,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
