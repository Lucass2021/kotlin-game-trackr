package com.lucasdias.gametrackr.feature.auth.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.components.AuthScreenScaffold
import com.lucasdias.gametrackr.core.ui.components.BackButton
import com.lucasdias.gametrackr.core.ui.components.PrimaryButton
import com.lucasdias.gametrackr.core.ui.components.SocialLoginSection
import com.lucasdias.gametrackr.core.ui.components.TitleWithSubtitle
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme
import com.lucasdias.gametrackr.feature.auth.login.components.LoginFormSection
import com.lucasdias.gametrackr.feature.auth.login.components.RememberMeRow
import com.lucasdias.gametrackr.feature.auth.login.components.SignUpPrompt

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onSignUp: () -> Unit
) {
    val form = remember { LoginFormState() }

    AuthScreenScaffold(scrollable = false) {
        BackButton(onBack = onBack, modifier = Modifier.staggeredAppear(0))

        Spacer(modifier = Modifier.weight(1f))

        TitleWithSubtitle(
            title = stringResource(R.string.login_title),
            subtitle = stringResource(R.string.login_subtitle),
            centered = true,
            modifier = Modifier
                .padding(top = 16.dp)
                .staggeredAppear(1)
        )

        LoginFormSection(
            form = form,
            modifier = Modifier
                .padding(top = 28.dp)
                .staggeredAppear(2)
        )

        RememberMeRow(
            rememberMe = form.rememberMe,
            onToggle = { form.rememberMe = !form.rememberMe },
            onForgotPassword = { form.forgotPassword() },
            modifier = Modifier
                .padding(top = 20.dp)
                .staggeredAppear(3)
        )

        PrimaryButton(
            text = stringResource(R.string.login_sign_in),
            onClick = { form.submit() },
            isLoading = form.isLoading,
            modifier = Modifier
                .padding(top = 24.dp)
                .staggeredAppear(4)
        )

        SocialLoginSection(
            onGoogle = { form.signInGoogle() },
            modifier = Modifier
                .padding(top = 28.dp)
                .staggeredAppear(5)
        )

        SignUpPrompt(
            onSignUp = onSignUp,
            modifier = Modifier
                .padding(top = 28.dp)
                .staggeredAppear(6)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    GameTrackrTheme {
        LoginScreen(onBack = {}, onSignUp = {})
    }
}
