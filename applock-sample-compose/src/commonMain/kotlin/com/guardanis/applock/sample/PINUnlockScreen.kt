package com.guardanis.applock.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.guardanis.applock.sample.theme.AppTheme
import com.guardanis.applock.settings.PINConfig
import com.guardanis.applock.views.PINUnlockView

class PINUnlockScreen(
    val config: PINConfig,
    val completionStatusUpdateMessage: MutableState<String>
): Screen {

    @Composable
    override fun Content() = AppTheme({
        val navigator = LocalNavigator.current!!

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(config.theme.itemBackgroundColor)
                .padding(12.dp),
            contentAlignment = Alignment.Center,
            content = {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp)),
                    content = {
                        PINUnlockView(
                            config = config,
                            onUnlocked = {
                                completionStatusUpdateMessage.value = "PIN Unlock Success"

                                navigator.pop()
                            }
                        )
                    }
                )
            }
        )
    })
}