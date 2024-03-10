package com.guardanis.applock.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.guardanis.applock.settings.PINConfig
import com.guardanis.applock.settings.PINTheme

@Composable
@Preview(widthDp = 320, heightDp = 50)
fun PINInputViewPreview(
    config: PINConfig = PINConfig()
) {

    PINInputView(
        config = config,
        inputSessionKey = "",
        onInputEntered = { }
    )
}

@Composable
@Preview(widthDp = 320, heightDp = 50)
fun PINInputViewPreviewExposed(
    config: PINConfig = PINConfig()
) {

    PINInputView(
        config = PINConfig(
            theme = PINTheme(
                passwordCharactersEnabled = false
            )
        ),
        inputSessionKey = "",
        onInputEntered = { }
    )
}