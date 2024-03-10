package com.guardanis.applock.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.guardanis.applock.settings.Config

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SampleApp() {
    Navigator(
        SampleHomeScreen(
            config = Config()
        ),
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "AppLock KMP Sample")
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF3C9ADF),
                            titleContentColor = Color.White
                        )
                    )
                },
                content = { padding ->
                    Box(
                        modifier = Modifier.padding(padding),
                        content = {
                            CurrentScreen()
                        }
                    )
                }
            )
        }
    )
}