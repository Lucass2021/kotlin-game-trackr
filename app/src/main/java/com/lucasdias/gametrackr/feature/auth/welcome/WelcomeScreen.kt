package com.lucasdias.gametrackr.feature.auth.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasdias.gametrackr.R
import com.lucasdias.gametrackr.core.ui.anim.staggeredAppear
import com.lucasdias.gametrackr.core.ui.anim.subtleBounce
import com.lucasdias.gametrackr.core.ui.theme.AppTextSecondary
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme
import com.lucasdias.gametrackr.feature.auth.welcome.components.WelcomeBackground
import com.lucasdias.gametrackr.feature.auth.welcome.components.WelcomeBottomSection

@Composable
fun WelcomeScreen(
    onCreateAccount: () -> Unit,
    onSignIn: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        WelcomeBackground()

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 24.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth().staggeredAppear(0)) {
                Image(
                    painter = painterResource(R.drawable.logo_wordmark),
                    contentDescription = "GameTrackr",
                    modifier = Modifier.height(28.dp),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth().staggeredAppear(1),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_hero),
                    contentDescription = "GameTrackr",
                    modifier = Modifier.fillMaxWidth(0.90f).subtleBounce(),
                    contentScale = ContentScale.Fit,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.welcome_tagline),
                    color = AppTextSecondary,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            WelcomeBottomSection(
                onCreateAccount = onCreateAccount,
                onSignIn = onSignIn,
                modifier = Modifier.staggeredAppear(2),
            )
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    GameTrackrTheme {
        WelcomeScreen(onCreateAccount = {}, onSignIn = {})
    }
}
