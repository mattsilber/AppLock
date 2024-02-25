package com.guardanis.applock.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.guardanis.applock.settings.Config
import com.guardanis.applock.settings.PINTheme
import com.guardanis.applock.settings.Theme

@Composable
@Preview(widthDp = 320, heightDp = 250)
fun PINCreationViewPreview(
    config: Config = Config()
) {

    PINCreationView(
        config = config,
        onInputEntered = { }
    )
}