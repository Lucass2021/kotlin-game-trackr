package com.lucasdias.gametrackr.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.core.ui.icon.AppIcon
import com.lucasdias.gametrackr.core.ui.theme.AppOnPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    icon: AppIcon? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(16.dp)
    Button(
        onClick = onClick,
        enabled = !isLoading,
        interactionSource = interactionSource,
        modifier =
            modifier
                .pressScale(interactionSource)
                .fillMaxWidth()
                .height(54.dp)
                .glow(color = AppPrimary, cornerRadius = 16.dp),
        shape = shape,
        elevation =
            ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp,
            ),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = AppPrimary,
                contentColor = AppOnPrimary,
                disabledContainerColor = AppPrimary,
                disabledContentColor = AppOnPrimary,
            ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = AppOnPrimary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp),
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon.image(),
                        contentDescription = null,
                        tint = AppOnPrimary,
                        modifier = Modifier.size(20.dp),
                    )
                }
                Text(text = text, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
