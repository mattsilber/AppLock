package com.guardanis.applock.sample

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.guardanis.applock.sample.theme.AppTheme
import com.guardanis.applock.settings.Config
import com.guardanis.applock.views.PINUnlockView

class PINUnlockScreen(
    val config: Config,
    val onUnlocked: () -> Unit
): Screen {

    @Composable
    override fun Content() = AppTheme({
        PINUnlockView(
            config = config,
            onUnlocked = onUnlocked
        )
    })
}