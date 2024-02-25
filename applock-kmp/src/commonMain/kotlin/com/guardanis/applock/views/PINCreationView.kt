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

    val page = remember<PINCreationPage>({ PINCreationPage.CREATE })
    val unconfirmedInput = remember<String>({ "" })
    val errorText = remember<String>({ "" })

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
                    // TODO: Verify valid, submit to service
                }
            )
        }
    )
}