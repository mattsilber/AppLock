package com.guardanis.applock.settings

import androidx.compose.ui.graphics.Color

data class PINTheme(
    val pinItemCount: Int = 4,
    val passwordCharactersEnabled: Boolean = true,
    val itemBackgroundColor: Color = Color(0xFF3C9ADF),
    val itemForegroundColor: Color = Color(0xFFFFFFFF),
    val uiBackgroundColor: Color = Color(0xFFFAFAFA),
    val uiForegroundColor: Color = Color(0xFF7F8C8D),
)