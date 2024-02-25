package com.guardanis.applock.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.guardanis.applock.settings.Config

@Composable
fun PINInputView(
    config: Config
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = {
            Row(
                content = {
                    0
                        .until(config.theme.pinTheme.pinItemCount)
                        .map({
                            PINInputViewItem(
                                theme = config.theme.pinTheme,
                                value = it.toString()
                            )
                        })
                        .map({ item ->
                            Column(
                                content = { item }
                            )
                        })
                }
            )
        }
    )
}