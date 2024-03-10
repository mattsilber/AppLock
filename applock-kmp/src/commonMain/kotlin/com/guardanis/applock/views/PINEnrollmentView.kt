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

enum class PINEnrollmentPage {
    CREATE,
    CONFIRM
}

@Composable
fun PINEnrollmentView(
    config: Config,
    onLockCreated: () -> Unit,
) {

    var page by remember({ mutableStateOf(PINEnrollmentPage.CREATE) })
    var unconfirmedInput by remember({ mutableStateOf("") })
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
                text = when (page) {
                    PINEnrollmentPage.CREATE ->
                        errorText.takeIf(String::isNotEmpty) ?: config.language.pinCreation.createDescription
                    PINEnrollmentPage.CONFIRM ->
                        config.language.pinCreation.confirmDescription
                },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            PINInputView(
                config = config,
                inputSessionKey = inputSessionKey,
                onInputEntered = {
                    // Reset our input session key to revert the PIN view to an empty state
                    inputSessionKey = System.currentTimeMillis()

                    when (page) {
                        PINEnrollmentPage.CREATE -> {
                            if (it.length != config.pinTheme.pinItemCount) {
                                errorText = config.language.pinCreation.errorIncorrectLength

                                return@PINInputView
                            }

                            unconfirmedInput = it
                            page = PINEnrollmentPage.CONFIRM
                        }
                        PINEnrollmentPage.CONFIRM -> {
                            if (it.length != config.pinTheme.pinItemCount) {
                                errorText = config.language.pinCreation.errorIncorrectLength
                                page = PINEnrollmentPage.CREATE

                                return@PINInputView
                            }

                            if (it != unconfirmedInput) {
                                errorText = config.language.pinCreation.errorMismatch
                                page = PINEnrollmentPage.CREATE

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