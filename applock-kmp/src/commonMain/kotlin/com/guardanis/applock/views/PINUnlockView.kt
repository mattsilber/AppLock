package com.guardanis.applock.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.guardanis.applock.AppLock
import com.guardanis.applock.settings.Config

@Composable
fun PINUnlockView(
    config: Config,
    onUnlocked: () -> Unit
) {

    var errorText by remember({ mutableStateOf("") })
    var inputSessionKey by remember({ mutableLongStateOf(0L) })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(config.pinTheme.uiBackgroundColor)
            .padding(all = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Text(
                text = errorText.takeIf(String::isNotEmpty)
                    ?: config.language.pinUnlockLanguage.inputDescription,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            PINInputView(
                config = config,
                inputSessionKey = inputSessionKey,
                onInputEntered = {
                    inputSessionKey = System.currentTimeMillis()

                    AppLock.pinAuthenticate(
                        pin = it,
                        success = onUnlocked,
                        fail = {
                            errorText = config.language.pinUnlockLanguage.errorMismatch
                        }
                    )
                }
            )
        }
    )
}