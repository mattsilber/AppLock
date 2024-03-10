package com.guardanis.applock.sample

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.guardanis.applock.sample.theme.AppTheme
import com.guardanis.applock.settings.Config
import com.guardanis.applock.views.PINEnrollmentView

class PINEnrollmentScreen(
    val config: Config,
    val onLockCreated: () -> Unit
): Screen {

    @Composable
    override fun Content() = AppTheme({
        PINEnrollmentView(
            config = config,
            onLockCreated = onLockCreated
        )
    })
}