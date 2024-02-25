package com.guardanis.applock.views

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.guardanis.applock.settings.PINTheme

@Composable
fun PINInputViewItem(
    theme: PINTheme,
    value: String?
) {

    Box(
        modifier = Modifier.aspectRatio(1.0F, matchHeightConstraintsFirst = true),
        contentAlignment = Alignment.Center,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize(fraction = if (value == null) theme.itemMinSizePercent else 1.0F)
                    .background(
                        color = theme.itemBackgroundColor,
                        shape = CircleShape
                    )
                    .animateContentSize(),
                contentAlignment = Alignment.Center,
                content = {
                    Text(
                        text = if (theme.passwordCharactersEnabled) theme.passwordCharacter else (value ?: ""),
                        color = theme.itemForegroundColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    )
}