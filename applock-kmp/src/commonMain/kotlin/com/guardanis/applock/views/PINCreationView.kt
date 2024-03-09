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
import com.guardanis.applock.settings.Config

enum class PINCreationPage {
    CREATE,
    CONFIRM
}

@Composable
fun PINCreationView(
    config: Config,
    onLockCreated: () -> Unit,
) {

    var page = remember<PINCreationPage>({ PINCreationPage.CREATE })
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
                text = when (page) {
                    PINCreationPage.CREATE ->
                        errorText.takeIf(String::isNotEmpty) ?: config.language.pinCreation.createDescription
                    PINCreationPage.CONFIRM ->
                        config.language.pinCreation.confirmDescription
                },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            PINInputView(
                config = config,
                onInputEntered = {
                    when (page) {
                        PINCreationPage.CREATE -> {
                            if (it.length != config.pinTheme.pinItemCount) {
                                errorText = config.language.pinCreation.errorIncorrectLength

                                return@PINInputView
                            }

                            unconfirmedInput = it
                            page = PINCreationPage.CONFIRM
                        }
                        PINCreationPage.CONFIRM -> {
                            if (it.length != config.pinTheme.pinItemCount) {
                                errorText = config.language.pinCreation.errorIncorrectLength
                                page = PINCreationPage.CREATE

                                return@PINInputView
                            }

                            if (it != unconfirmedInput) {
                                errorText = config.language.pinCreation.errorMismatch
                                page = PINCreationPage.CREATE

                                return@PINInputView
                            }

                            AppLock.enrollPinAuthentication(pin = it)

                            onLockCreated()
                        }
                    }
                }
            )
        }
    )
}