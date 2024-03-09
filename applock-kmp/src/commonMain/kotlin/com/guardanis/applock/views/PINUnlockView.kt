package com.guardanis.applock.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.guardanis.applock.AppLock
import com.guardanis.applock.services.PINLockService
import com.guardanis.applock.settings.Config

enum class PINUnlockPage {
    INPUT,
    ERROR
}

@Composable
fun PINUnlockView(
    config: Config,
    onUnlocked: () -> Unit
) {

    var page = remember<PINUnlockPage>({ PINUnlockPage.INPUT })
    var unconfirmedInput = remember<String>({ "" })
    var errorText = remember<String>({ "" })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(config.pinTheme.uiBackgroundColor)
            .padding(all = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Text(
                text = errorText.takeIf(String::isNotEmpty)
                    ?: config.language.pinUnlockLanguage.inputDescription,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            PINInputView(
                config = config,
                onInputEntered = {
                    AppLock.pinAuthenticate(
                        pin = it,
                        success = onUnlocked,
                        fail = {
                            errorText = config.language.pinUnlockLanguage.errorMismatch
                            page = PINUnlockPage.ERROR
                        }
                    )
                }
            )
        }
    )
}