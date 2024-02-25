package com.guardanis.applock.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.guardanis.applock.settings.Config

@Composable
fun PINInputView(
    config: Config,
    onInputEntered: (String) -> Unit
) {

    var availableSize = remember<IntSize>({ IntSize(0, 0) })
    var input = remember<String>({ "" })

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
            TextField(
                value = "",
                onValueChange = {
                    input = it
                },
                modifier = Modifier
                    .alpha(0F)
                    .fillMaxSize(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onInputEntered(input)
                    }
                )
            )

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
                                            value = if (it < input.length) input[it].toString() else null
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