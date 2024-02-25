package com.guardanis.applock.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.guardanis.applock.settings.Config

@Composable
fun PINInputView(
    config: Config
) {

    var availableSize = remember<IntSize>({ IntSize(0, 0) })

    val kindOfHalfSpacer = availableSize
        .width
        .div(config.theme.pinTheme.pinItemCount)
        .times(availableSize.width * 0.05)
        .div(2)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged({
                availableSize = it
            }),
        contentAlignment = Alignment.Center,
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    0
                        .until(config.theme.pinTheme.pinItemCount)
                        .map({
                            listOf(
                                Spacer(
                                    modifier = Modifier.width(kindOfHalfSpacer.dp)
                                ),
                                Column(
                                    content = {
                                        PINInputViewItem(
                                            theme = config.theme.pinTheme,
                                            value = it.toString()
                                        )
                                    }
                                ),
                                Spacer(
                                    modifier = Modifier.width(kindOfHalfSpacer.dp)
                                ),
                            )
                        })
                }
            )
        }
    )
}