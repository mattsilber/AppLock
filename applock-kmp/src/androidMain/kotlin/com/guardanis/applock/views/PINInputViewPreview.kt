package com.guardanis.applock.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.guardanis.applock.settings.Config

@Composable
@Preview
fun PINInputViewPreview(
    config: Config = Config()
) {
    
    PINInputView(config = config)
}