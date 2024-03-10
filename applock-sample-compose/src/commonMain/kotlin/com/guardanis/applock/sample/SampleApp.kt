package com.guardanis.applock.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.guardanis.applock.sample.theme.AppTheme
import com.guardanis.applock.settings.PINConfig

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SampleApp() = AppTheme({
    Navigator(
        SampleHomeScreen(
            config = PINConfig()
        ),
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "AppLock KMP Sample")
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
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
})