package com.guardanis.applock.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.guardanis.applock.settings.Config

@Composable
fun PINInputView(
    config: Config,
    inputSessionKey: Any,
    onInputEntered: (String) -> Unit
) {

    var input by remember(inputSessionKey, { mutableStateOf("") })
    val inputPattern = Regex("^[0-9]{0,${config.pinTheme.pinItemCount}}\$")
    val focusRequester = remember(::FocusRequester)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(config.pinTheme.pinViewHeight),
        contentAlignment = Alignment.Center,
        content = {
            TextField(
                value = input,
                onValueChange = {
                    input = if (it.matches(inputPattern)) it else input
                },
                modifier = Modifier
                    .alpha(0F)
                    .fillMaxSize()
                    .focusRequester(focusRequester),
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
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(config.pinTheme.pinItemSpacer),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Spacer(modifier = Modifier.weight(1F))
                    repeat(
                        config.pinTheme.pinItemCount,
                        { index ->
                            Column(
                                content = {
                                    PINInputViewItem(
                                        theme = config.pinTheme,
                                        value = if (index < input.length) input[index].toString() else null
                                    )
                                }
                            )
                        }
                    )
                    Spacer(modifier = Modifier.weight(1F))
                }
            )

            LaunchedEffect(inputSessionKey, { focusRequester.requestFocus() })
        }
    )
}