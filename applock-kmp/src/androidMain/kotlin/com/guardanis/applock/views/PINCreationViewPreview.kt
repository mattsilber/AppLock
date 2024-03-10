package com.guardanis.applock.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.guardanis.applock.settings.PINConfig

@Composable
@Preview(widthDp = 320, heightDp = 250)
fun PINCreationViewPreview(
    config: PINConfig = PINConfig()
) {

    PINEnrollmentView(
        config = config,
        onLockCreated = { }
    )
}