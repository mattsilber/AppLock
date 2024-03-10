package com.guardanis.applock.settings

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PINTheme(
    val pinItemCount: Int = 4,
    val pinViewHeight: Dp = 50.dp,
    val pinItemSpacer: Dp = 8.dp,
    val passwordCharactersEnabled: Boolean = true,
    val passwordCharacter: String = "*",
    val itemMinSizePercent: Float = 0.05F,
    val itemBackgroundColor: Color = Color(0xFF3C9ADF),
    val itemForegroundColor: Color = Color(0xFFFFFFFF),
    val uiBackgroundColor: Color = Color(0xFFFAFAFA),
    val uiForegroundColor: Color = Color(0xFF7F8C8D),
)