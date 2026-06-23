package com.lucasdias.gametrackr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lucasdias.gametrackr.core.ui.theme.GameTrackrTheme
import com.lucasdias.gametrackr.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameTrackrTheme {
                AppNavGraph()
            }
        }
    }
}
