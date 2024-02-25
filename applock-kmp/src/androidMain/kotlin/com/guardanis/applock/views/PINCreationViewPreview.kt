package com.guardanis.applock.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.guardanis.applock.settings.Config

@Composable
@Preview(widthDp = 320, heightDp = 250)
fun PINCreationViewPreview(
    config: Config = Config()
) {

    PINCreationView(
        config = config,
        onLockCreated = { }
    )
}