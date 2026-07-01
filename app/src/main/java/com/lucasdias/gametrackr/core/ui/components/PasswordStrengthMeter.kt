package com.lucasdias.gametrackr.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.theme.AppOutline
import com.lucasdias.gametrackr.core.ui.theme.AppPrimary
import com.lucasdias.gametrackr.core.ui.theme.AppSecondary
import com.lucasdias.gametrackr.core.ui.theme.AppTertiary

enum class PasswordStrength(
    val level: Int,
    @StringRes val label: Int,
    val color: Color,
) {
    WEAK(1, R.string.password_strength_weak, AppTertiary),
    MEDIUM(2, R.string.password_strength_medium, AppSecondary),
    STRONG(3, R.string.password_strength_strong, AppPrimary),
    ;

    companion object {
        fun of(password: String): PasswordStrength {
            if (password.isEmpty()) return WEAK

            var score = 0
            if (password.length >= 6) score++
            if (password.any { it.isUpperCase() } && password.any { it.isLowerCase() }) score++
            if (password.any { it.isDigit() }) score++
            if (password.any { !it.isLetterOrDigit() }) score++

            return when (score) {
                0, 1 -> WEAK
                2, 3 -> MEDIUM
                else -> STRONG
            }
        }
    }
}

@Composable
fun PasswordStrengthMeter(
    strength: PasswordStrength,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            repeat(3) { index ->
                val target = if (index < strength.level) strength.color else AppOutline
                val color by animateColorAsState(targetValue = target, label = "strengthSegment")
                Spacer(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(5.dp)
                            .background(color, CircleShape),
                )
                if (index < 2) Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(strength.label),
            color = strength.color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 2.dp),
        )
    }
}
